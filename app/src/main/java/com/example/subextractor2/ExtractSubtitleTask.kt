package com.example.subextractor2

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Xml
import android.widget.TextView
import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.downloader.request.RequestSubtitlesDownload
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo
import com.github.kiulian.downloader.downloader.response.Response
import com.github.kiulian.downloader.model.Extension
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo
import com.github.kiulian.downloader.model.videos.VideoInfo
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.logging.XMLFormatter

open class ExtractSubtitleTask(private var context: Context?, private var textView: TextView?) : AsyncTask<String, Void, String>() {

    private var progress : ProgressDialog = ProgressDialog(context)
    override fun onPreExecute() {
        super.onPreExecute()
        progress.setTitle("Thông báo")
        progress.setMessage("Đang xử lý")
        progress.show()
    }

    override fun doInBackground(vararg params: String?): String? {

        try {
            val builder : StringBuilder = StringBuilder()
            val downloader = YoutubeDownloader()
            val request = RequestVideoInfo(params[0])
            val response: Response<VideoInfo> = downloader.getVideoInfo(request)
            val video = response.data()


            val subtitlesInfo: List<SubtitlesInfo> = video.subtitlesInfo() // NOTE: includes auto-generated

            for (info in subtitlesInfo) {
                val request = RequestSubtitlesDownload(info) // optional
                    .formatTo(Extension.JSON3)
                    .translateTo("en")
                val response = downloader.downloadSubtitle(request)
                val subtitlesString = response.data()

                val jsonObject = JSONObject(subtitlesString)
                val jsonArray  = jsonObject.getJSONArray("events")

                for(i in 0 until jsonArray.length()){
                    if (jsonArray.getJSONObject(i).has("segs")){
                        val segs = jsonArray.getJSONObject(i).getJSONArray("segs")
                        for (j in 0 until segs.length()){
                            val utf8 = segs.getJSONObject(j).getString("utf8")
                            builder.append(utf8)
                        }
                    }
                }
            }
            return builder.toString()
        }catch (ex : Exception){
            ex.printStackTrace()
            return "Đã có lỗi xảy ra"
        }
    }


    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        progress?.dismiss()
        textView?.text = result
    }
}