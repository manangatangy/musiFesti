package com.manangatangy.musifesti.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.manangatangy.musifesti.R
import com.manangatangy.musifesti.databinding.ItemBandBinding
import com.manangatangy.musifesti.databinding.ItemFestivalBinding
import com.manangatangy.musifesti.databinding.ItemRecordLabelBinding
import com.manangatangy.musifesti.databinding.ItemSpacerBinding

class FestivalsAdapter(
    private val items: List<DisplayItem>,
    private val context: Context)
    : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        private const val ITEM_TYPE_RECORD_LABEL = 1
        private const val ITEM_TYPE_BAND = 2
        private const val ITEM_TYPE_FESTIVAL = 3
        private const val ITEM_TYPE_SPACER = 4
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when(viewType) {
            ITEM_TYPE_RECORD_LABEL -> RecordLabelViewHolder(parent, inflater)
            ITEM_TYPE_BAND -> BandViewHolder(parent, inflater)
            ITEM_TYPE_FESTIVAL -> MusicFestivalViewHolder(parent, inflater)
            ITEM_TYPE_SPACER -> SpacerViewHolder(parent, inflater)
            else -> throw Exception("View type not supported $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        when(holder) {
            is RecordLabelViewHolder -> holder.bind(item as DisplayItem.RecordLabelItem)
            is BandViewHolder -> holder.bind(item as DisplayItem.BandItem)
            is MusicFestivalViewHolder -> holder.bind(item as DisplayItem.MusicFestivalItem)
            is SpacerViewHolder -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int = when(items[position]) {
        is DisplayItem.RecordLabelItem -> ITEM_TYPE_RECORD_LABEL
        is DisplayItem.BandItem -> ITEM_TYPE_BAND
        is DisplayItem.MusicFestivalItem -> ITEM_TYPE_FESTIVAL
        is DisplayItem.SpacerItem -> ITEM_TYPE_SPACER
    }
}

sealed class DisplayItem {
    data class RecordLabelItem(val recordLabelName: String) : DisplayItem()
    data class BandItem(val bandName: String) : DisplayItem()
    data class MusicFestivalItem(val festivalName: String) : DisplayItem()
    object SpacerItem : DisplayItem()
}

class RecordLabelViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val binding: ItemRecordLabelBinding = DataBindingUtil.inflate(inflater,
        R.layout.item_record_label,
        parent, false)
) : ViewHolder(binding.root) {
    fun bind(recordLabelItem: DisplayItem.RecordLabelItem) {
        binding.apply {
            recordLabelName = recordLabelItem.recordLabelName
        }
    }
}

class BandViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val binding: ItemBandBinding = DataBindingUtil.inflate(inflater,
        R.layout.item_band,
        parent, false)
) : ViewHolder(binding.root) {
    fun bind(bandItem: DisplayItem.BandItem) {
        binding.apply {
            bandName = bandItem.bandName
        }
    }
}

class MusicFestivalViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val binding: ItemFestivalBinding = DataBindingUtil.inflate(inflater,
        R.layout.item_festival,
        parent, false)
) : ViewHolder(binding.root) {
    fun bind(festivalItem: DisplayItem.MusicFestivalItem) {
        binding.apply {
            festivalName = festivalItem.festivalName
        }
    }
}

class SpacerViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    private val binding: ItemSpacerBinding = DataBindingUtil.inflate(inflater,
        R.layout.item_spacer,
        parent, false)
) : ViewHolder(binding.root)
