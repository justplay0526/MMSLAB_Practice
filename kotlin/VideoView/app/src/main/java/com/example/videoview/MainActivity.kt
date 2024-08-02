package com.example.videoview

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoview.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val subtitleObj = ArrayList<SubtitleObj.Result.VideoInfo.CaptionResult.Results.Captions>()
    private lateinit var youTubePlayers: YouTubePlayer
    private var buttonState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getSubtitle()

        lifecycle.addObserver(binding.youtubePlayerView)

        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayers = youTubePlayer
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                super.onStateChange(youTubePlayer, state)
                when (state) {
                    PlayerConstants.PlayerState.PLAYING -> {
                        binding.btnTest.setBackgroundResource(R.drawable.baseline_pause_24)
                    }

                    PlayerConstants.PlayerState.PAUSED -> {
                        binding.btnTest.setBackgroundResource(R.drawable.baseline_play_arrow_24)
                    }
                    else -> {}
                }
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
//                val pos = findPos(second)
//                Log.e("debug", "onCurrentSecond: $second\ncurrentPosition: $pos\n")
                (binding.rcvSentence.adapter as SubtitleAdapter?)?.setCurrentPosition(findPos(second))
            }
        })

        binding.btnTest.setOnClickListener {
            if (buttonState) {
                youTubePlayers.pause()
                buttonState = false
                binding.btnTest.setBackgroundResource(R.drawable.baseline_play_arrow_24)
            } else {
                youTubePlayers.play()
                buttonState = true
                binding.btnTest.setBackgroundResource(R.drawable.baseline_pause_24)

            }

        }
    }

    private fun getSubtitle() {
        val guestKey = "44f6cfed-b251-4952-b6ab-34de1a599ae4"
        val videoId = "5edfb3b04486bc1b20c2851a"
        val mode = 0

        val url = "https://api.italkutalk.com/api/video/detail"
        val json = "{\"guestKey\": \"${guestKey}\"," +
                "\"videoID\": \"${videoId}\"," +
                "\"mode\": ${mode},}"
        val body = json.toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())

        val req = Request.Builder()
            .url(url)
            .post(body)
            .build()

        OkHttpClient().newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure", "$e")
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                val resObj = Gson().fromJson(res, SubtitleObj::class.java)

                subtitleObj.clear()
                subtitleObj.addAll(resObj.result.videoInfo.captionResult.results[0].captions)
                Log.d("subtitleObj", "$subtitleObj")
                runOnUiThread {
                    val subtitleAdapter = SubtitleAdapter(subtitleObj).apply {
                        onItemClick = { item ->
                            Toast.makeText(this@MainActivity, item.miniSecond.toString(), Toast.LENGTH_SHORT).show()
                            val second = item.miniSecond.toFloat()
                            Log.e("debug", "$second seconds")
                            youTubePlayers.seekTo(second)
                            (binding.rcvSentence.adapter as SubtitleAdapter?)?.setCurrentPosition(findPos(second))
                        }
                    }
                    binding.rcvSentence.adapter = subtitleAdapter
                    val dividerItemDecoration = DividerItemDecoration(binding.rcvSentence.context, (binding.rcvSentence.layoutManager as LinearLayoutManager).orientation)
                    binding.rcvSentence.addItemDecoration(dividerItemDecoration)
                }
            }
        })
    }

    private fun findPos(t: Float): Int {
        val size = subtitleObj.size
        subtitleObj.forEachIndexed { idx, obj ->
            if (idx == size - 1) //最後一個idx判斷獨立處理
                if (t >= obj.miniSecond) return idx else idx-1

            if (t < obj.miniSecond)
                return idx - 1
        }
        return -1 //錯誤, 找不到位置
    }


    override fun onDestroy() {
        super.onDestroy()
        // 確保在銷毀時釋放資源
        binding.youtubePlayerView.release()
    }
}