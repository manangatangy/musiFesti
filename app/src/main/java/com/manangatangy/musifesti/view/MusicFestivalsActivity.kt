package com.manangatangy.musifesti.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.manangatangy.musifesti.R
import com.manangatangy.musifesti.databinding.ActivityMusicFestivalsListBinding
import com.manangatangy.musifesti.model.ApiResult
import com.manangatangy.musifesti.model.MusicFestival
import com.manangatangy.musifesti.viewmodel.MusicFestivalsViewModel
import com.manangatangy.musifesti.viewmodel.makeDisplayItems

class MusicFestivalsActivity : AppCompatActivity() {

    private lateinit var viewModel: MusicFestivalsViewModel
    private lateinit var binding: ActivityMusicFestivalsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {

//        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

//        viewModel = ViewModelProviders.of(this).get(GroceryListViewModel::class.java)
        viewModel = ViewModelProvider(this).get(MusicFestivalsViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_music_festivals_list)
        binding.rvBandList.layoutManager = LinearLayoutManager(this)

        viewModel.getFestivals.observe(this, Observer { apiResult ->
            when(apiResult) {
                is ApiResult.Ok<List<MusicFestival>> -> {
                    val displayItems = makeDisplayItems(apiResult.value)
                    binding.rvBandList.adapter = FestivalsAdapter(displayItems, this)
                }
                is ApiResult.HttpError<*> -> {
                    val code = apiResult.httpException.code()
                    val message = apiResult.httpException.message()
                    val bytes = apiResult.response?.errorBody()?.bytes()
                    val body = bytes?.let {
                        String(bytes)
                    } ?: ""
                    println("httpError code=$code message=[$message] body=[$body]")
                }
            }
//            binding.tvSummary.text = it.title
            // ApiResult<List<MusicFestival>>
            //        val displayItems = makeDisplayItems(repository.getFestivals())
            // ApiResult<List<MusicFestival>>
//        Log.d("MusicFestivalsViewModel", displayItems.toText())

        })

//        showFirstTodo()
    }

//    private fun showFirstTodo() {
//        viewModel.getFirstTodo.observe(this, Observer {
//            binding.tvSummary.text = it.title
//        })
//    }

}
