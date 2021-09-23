package com.example.subextractor2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.subextractor2.databinding.ActivityMainBinding
import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.downloader.request.RequestSubtitlesDownload
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo
import com.github.kiulian.downloader.downloader.response.Response
import com.github.kiulian.downloader.model.Extension
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo
import com.github.kiulian.downloader.model.videos.VideoInfo


class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)


        binding.let {
            bindingNotNull ->
            bindingNotNull?.btnSubExtractor?.setOnClickListener(View.OnClickListener {
                ExtractSubtitleTask(this,bindingNotNull.tvSubResult).execute(convertYoutubeLink(bindingNotNull.edtPasteLink.text.toString().trim()))
            })
        }
    }

    private fun convertYoutubeLink(youtubeLink : String) : String{
        return youtubeLink.removePrefix("https://www.youtube.com/watch?v=")
    }
}