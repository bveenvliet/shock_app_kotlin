package com.example.countershockkotlin

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_suprise.*
import java.io.File

class SurpriseActivity : AppCompatActivity() {
    private lateinit var photoUri: Uri
    private lateinit var soundUri: Uri
    private lateinit var tts: TextToSpeech
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var imageModel: ImageModel
    private lateinit var audioModel: AudioModel
    private var acceptTouches: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suprise)

        imageModel = ImageStorer(this).getSelectedImage()
        audioModel = AudioStorer(this).getSelectedAudio()

        photoUri = if (imageModel.isAsset) {
            ShockUtils.getDrawableUri(this, imageModel.imgFilename)
        } else {
            Uri.fromFile(File(imageModel.imgFilename))
        }

        if (!audioModel.isTTS) {
            soundUri = ShockUtils.getRawUri(this, audioModel.audioFilename)
        }

        Toast.makeText(this, "Ready", Toast.LENGTH_SHORT).show()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun showImage() {
        Glide.with(this)
            .load(photoUri)
            .into(imageViewSurprise)
        imageViewSurprise.visibility = View.VISIBLE
    }

    private fun playSoundClip() {
        mediaPlayer = MediaPlayer.create(this, soundUri)
        mediaPlayer.setOnCompletionListener {
            finish()
        }
        mediaPlayer.start()
    }

    @Suppress("DEPRECATION")
    private fun handleTTS() {
        val toSpeak = audioModel.descriptionMessage
        tts = TextToSpeech(this, TextToSpeech.OnInitListener { p0 ->
            val params = HashMap<String, String>()

            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "utterId"

            if (p0 == TextToSpeech.SUCCESS) {
                tts.setOnUtteranceCompletedListener {
                    finish()
                }
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, params)
            } else {
                finish()
            }
        })
    }

    private fun userTriggeredAction() {
        if (!acceptTouches) return
        acceptTouches = false

        if (audioModel.isTTS) {
            handleTTS()
        } else {
            playSoundClip()
        }
        showImage()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        userTriggeredAction()
        return super.onTouchEvent(event)
    }

}