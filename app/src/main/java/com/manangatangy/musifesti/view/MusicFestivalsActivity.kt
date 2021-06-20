package com.manangatangy.musifesti.view

import android.os.Bundle
import android.util.Log
import android.view.View
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

    private lateinit var binding: ActivityMusicFestivalsListBinding
    private lateinit var viewModel: MusicFestivalsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_music_festivals_list)
        viewModel = ViewModelProvider(this).get(MusicFestivalsViewModel::class.java)

        binding.rvBandList.layoutManager = LinearLayoutManager(this)
        binding.bnReload.setOnClickListener { view -> fetchFestivals() }

        fetchFestivals()        // Initial load
    }

    private fun fetchFestivals() {
        viewModel.getFestivals.observe(this, Observer { apiResult ->
            when(apiResult) {
                is ApiResult.Loading -> {
                    binding.groupListLoaded.visibility = View.GONE
                    binding.groupError.visibility = View.GONE
                    binding.llLoading.visibility = View.VISIBLE
                }
                is ApiResult.Success<List<MusicFestival>> -> {
                    val displayItems = makeDisplayItems(apiResult.value)
                    binding.rvBandList.adapter = FestivalsAdapter(displayItems, this)
                    binding.llLoading.visibility = View.GONE
                    binding.groupError.visibility = View.GONE
                    binding.groupListLoaded.visibility = View.VISIBLE
                    Log.d("MusicFestivalsActivity","success -> displayItems count=${displayItems.size}")
                }
                is ApiResult.HttpError<*> -> {
                    val code = apiResult.httpException.code()
                    val message = apiResult.httpException.message()
                    val bytes = apiResult.response?.errorBody()?.bytes()
                    val body = bytes?.let { String(bytes) } ?: ""
                    binding.llLoading.visibility = View.GONE
                    binding.groupListLoaded.visibility = View.GONE
                    binding.groupError.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = getString(R.string.http_error_txt_text, code, message, body)
                    Log.d("MusicFestivalsActivity","httpError -> code=$code message=[$message] body=[$body]")
                }
                is ApiResult.Error -> {
                    val exception = apiResult.exception
                    binding.llLoading.visibility = View.GONE
                    binding.groupListLoaded.visibility = View.GONE
                    binding.groupError.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = getString(R.string.error_txt_text, exception)
                    Log.d("MusicFestivalsActivity","error -> exception=$exception")
                }
            }
        })
    }
}
