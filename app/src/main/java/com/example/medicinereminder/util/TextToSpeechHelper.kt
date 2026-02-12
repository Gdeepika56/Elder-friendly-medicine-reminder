package com.example.medicinereminder.util

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume


class TextToSpeechHelper(private val context: Context) {

    private var tts: TextToSpeech? = null

    suspend fun speak(text: String) {
        suspendCancellableCoroutine<Unit> { cont ->

            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.setSpeechRate(0.9f)

                    tts?.speak(
                        text,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "MEDICINE_TTS"
                    )
                }
                if(cont.isActive){
                    cont.resume(Unit)

                }
            }
            cont.invokeOnCancellation {
                tts?.stop()
                tts?.shutdown()
            }
        }
    }

    fun shutdown(){
        tts?.stop()
        tts?.shutdown()
    }
}