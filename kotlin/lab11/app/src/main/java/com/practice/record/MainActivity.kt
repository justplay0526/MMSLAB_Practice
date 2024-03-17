package com.practice.record

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.practice.record.databinding.ActivityMainBinding
import java.io.File
import java.lang.Exception
import java.util.Calendar

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private val recorder = MediaRecorder()
    private val player  = MediaPlayer()
    private lateinit var folder : File
    private var fileName = ""

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && requestCode == 0){
            val result = grantResults[0]
            if (result == PackageManager.PERMISSION_DENIED)
                finish()
            else{
                setFolder()
                setListener()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permission = android.Manifest.permission.RECORD_AUDIO
        if (ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
        } else {
            setFolder()
            setListener()
        }
    }

    override fun onDestroy() {
        recorder.release()
        player.release()
        super.onDestroy()
    }

    private fun setFolder(){
        folder = File(filesDir.absolutePath+"/record")
        if (!folder.exists()){
            folder.mkdirs()
        }
    }

    private fun setListener(){
        binding.btnRecord.setOnClickListener {
            fileName = "${Calendar.getInstance().time.time}"
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder.setOutputFile(File(folder, fileName).absolutePath)
            recorder.prepare()
            recorder.start()
            binding.tvTitle.text = "錄音中..."

            binding.btnRecord.isEnabled = false
            binding.btnStopRecord.isEnabled = true
            binding.btnPlay.isEnabled = false
            binding.btnStopPlay.isEnabled = false
        }

        binding.btnStopRecord.setOnClickListener {
            try {
                val file = File(folder, fileName)
                recorder.stop()
                binding.tvTitle.text = "以儲存至${file.absolutePath}"

                binding.btnRecord.isEnabled = true
                binding.btnStopRecord.isEnabled = false
                binding.btnPlay.isEnabled = true
                binding.btnStopPlay.isEnabled = false
            } catch (e: Exception){
                e.printStackTrace()
                recorder.reset()
                binding.tvTitle.text = "錄音失敗"

                binding.btnRecord.isEnabled = true
                binding.btnStopRecord.isEnabled = false
                binding.btnPlay.isEnabled = false
                binding.btnStopPlay.isEnabled = false
            }
        }

        binding.btnPlay.setOnClickListener {
            val file = File(folder, fileName)
            player.setDataSource(applicationContext, Uri.fromFile(file))
            player.setVolume(1f,1f)
            player.prepare()
            player.start()
            binding.tvTitle.text = "播放中..."

            binding.btnRecord.isEnabled = false
            binding.btnStopRecord.isEnabled = false
            binding.btnPlay.isEnabled = false
            binding.btnStopPlay.isEnabled = true
        }
        binding.btnStopPlay.setOnClickListener {
            player.stop()
            player.reset()
            binding.tvTitle.text = "播放結束"

            binding.btnRecord.isEnabled = true
            binding.btnStopRecord.isEnabled = false
            binding.btnPlay.isEnabled = true
            binding.btnStopPlay.isEnabled = false
        }

        player.setOnCompletionListener {
            it.reset()
            binding.tvTitle.text = "播放結束"

            binding.btnRecord.isEnabled = true
            binding.btnStopRecord.isEnabled = false
            binding.btnPlay.isEnabled = false
            binding.btnStopPlay.isEnabled = false
        }
    }
}