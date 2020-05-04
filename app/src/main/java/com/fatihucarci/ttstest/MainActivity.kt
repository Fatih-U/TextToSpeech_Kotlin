package com.fatihucarci.ttstest

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        tts!!.setSpeechRate(1f)
        btnSpeak.setOnClickListener {
             /**
             * This thread is for text to speech.
             */

            Thread {
                for (i in 4 downTo 0) {
                    if (i>0) speakOut(i.toString()) else speakOut("Başla")
                    Thread.sleep(1000)
                }
                /**
                 * This is for playing sound in given duration.
                 */
                playSoundForXSeconds(R.raw.fast_30secs,10)
            }.start()

        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set any language for tts
            val result = tts!!.setLanguage(Locale("tr-TR"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                btnSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

    private fun speakOut(text:String) {
        tts!!.speak(text, TextToSpeech.QUEUE_ADD, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    private fun playSoundForXSeconds(mp3File : Int, seconds: Int) {
        if (mp3File != null) {
            var mediaPlayer: MediaPlayer? = MediaPlayer.create(this, mp3File)
            mediaPlayer?.start() // no need to call prepare(); create() does that for you

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                mediaPlayer?.stop()
                mediaPlayer?.release()
            }, seconds * 1000.toLong())


        }
    }
}
