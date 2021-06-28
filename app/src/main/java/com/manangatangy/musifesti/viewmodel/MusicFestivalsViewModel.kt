package com.manangatangy.musifesti.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.manangatangy.musifesti.model.ApiResult
import com.manangatangy.musifesti.model.MusicFestival
import com.manangatangy.musifesti.model.Repository
import com.manangatangy.musifesti.view.DisplayItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MusicFestivalsViewModel : ViewModel(), KoinComponent {

    private val repository: Repository by inject()

    private val _festivals = MutableLiveData<ApiResult<List<MusicFestival>>>()
    val festivals: LiveData<ApiResult<List<MusicFestival>>> = _festivals

    fun retrieveFestivals() {
        viewModelScope.launch {
            _festivals.value = ApiResult.Loading
        }
        viewModelScope.launch {
            val fests = withContext(Dispatchers.IO) {
                repository.getFestivals()
            }
            _festivals.value = fests    // runs with Dispatchers.Main.immediate dispatcher
        }
    }
}

/**
 * Construct list of display items from the repository data
 */
fun makeDisplayItems(festivals: List<MusicFestival>): List<DisplayItem> {
    val tree = TreeNode("root")

    festivals.forEach { musicFestival ->
        musicFestival.bands?.forEach{ band ->
            tree.add(childName = band.recordLabel,
                grandChildName = band.name,
                greatGrandChildName = musicFestival.name)
        }
    }

    // Flatten the tree into a display list
    val displayItems: MutableList<DisplayItem> = mutableListOf()
    tree.forEachDepthFirst(0) { level, treeNode ->
        Log.d("MusicFestivalsViewModel", "level:${level}, name:${treeNode.name}")
        when(level) {
            1 -> {
                // Before each record-label, add a spacer (unless this is list first item)
                if (displayItems.isNotEmpty()) {
                    displayItems.add(DisplayItem.SpacerItem)
                }
                DisplayItem.RecordLabelItem(treeNode.name)
            }
            2 -> DisplayItem.BandItem(treeNode.name)
            3 -> DisplayItem.MusicFestivalItem(treeNode.name)
            else -> null        // Don't add starting level zero (root node) to the list
        }?.let { displayItems.add(it) }
    }
    return displayItems
}


/**
 * Extension function for diagnostics/testing use.
 */
fun List<DisplayItem>.toText() : String {
    val displayList = this
    return buildString {
        displayList.forEachIndexed { index, displayItem ->
            append(
                when (displayItem) {
                    is DisplayItem.RecordLabelItem -> "$index Label: ${displayItem.recordLabelName}\n"
                    is DisplayItem.BandItem -> "$index   Band: ${displayItem.bandName}\n"
                    is DisplayItem.MusicFestivalItem -> "$index     Festival: ${displayItem.festivalName}\n"
                    is DisplayItem.SpacerItem -> "$index   Spacer:\n"
                }
            )
        }
    }
}

/**
 * Tree of Strings, with no duplicate child names, at each parent node.
 * Represents the screen display of recordLabel/band/festival.
 *
 */
open class TreeNode(val name: String) {
    private val children: MutableSet<TreeNode> = mutableSetOf()

    // Create a new node, add it to the children and return it.
    // If a child with the same name already exists, don't create a
    // new one - just return the existing node.
    // Return null (and add nothing) if the childValue is null or empty
    private fun add(childName: String?): TreeNode? =
        if (childName.isNullOrEmpty()) null
        else childName.let {
            children.find { it.name == childName }?.let { return it }
            val child = TreeNode(childName)
            children.add(child)
            child
        }

    private fun add(childName: String?, grandChildName: String?) : TreeNode? =
        add(childName)?.add(grandChildName)

    // Add related recordLabel/band/festival nodes into the tree.
    // If an element name is null or empty, then neither it or it's descendants
    // are added (although all of it's non-null-or-empty ancestors are added).
    // This ensure no duplicate names within a node's children.
    fun add(childName: String?, grandChildName: String?, greatGrandChildName: String?) : TreeNode? =
        add(childName, grandChildName)?.add(greatGrandChildName)

    // Traverse the sorted tree, depth first.
    fun forEachDepthFirst(level: Int, visitor: Visitor) {
        visitor(level, this)
        children.sortedBy {
            it.name
        }.forEach {
            it.forEachDepthFirst(level + 1, visitor)
        }
    }
}

typealias Visitor = (Int, TreeNode) -> Unit
