package com.example.gptapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.SpannableString
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gptapp.databinding.AssistantActivityBinding
import java.util.Locale

class AssistantActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: AssistantActivityBinding
    private lateinit var textToSpeech: TextToSpeech

    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val result = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val text = result?.get(0)
            //mCompletionViewModel.postCompletionLiveData(text.toString())
            binding.pbWaiting.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AssistantActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoice.setOnClickListener {
            displaySpeechRecognizer()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale("ES"))
        }
    }

    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }

    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            // ACTION_RECOGNIZE_SPEECH. Inicia una actividad que solicitará voz al usuario y la enviará a través de un reconocedor de voz.
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,  // Informa al reconocedor qué modelo de voz preferir al realizar la interpretación. Lenguaje que queremos que se reconozca
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM   // Se Utilizará un modelo de lenguaje basado en reconocimiento de voz de forma libre.
            )

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,    // Informa la tiqueta de idioma IETF opcional, que queremos que se utilice en el contenido (textos) de la "Ventana Emergente", puede ser por ejemplo "en-US", "es-MX", etc.
                Locale.getDefault() // Locale.getDefault() Toma el idioma que tenemos configurado en el dispositivo. Se puede sustituir por la etiqueta de idioma IETF preferida.
            )

            putExtra(
                RecognizerIntent.EXTRA_PROMPT,  // Informa que se enviará un mensaje de texto opcional, para mostrar al usuario cuando se le pida que hable.
                "Pregunta lo que quieras"   // Mensaje de texto opcional
            )
        }

        try {
            startActivityForResult.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Su dispositivo no admite entrada por voz", Toast.LENGTH_LONG).show()
        }

    }

    private fun speakOut(response: String) {
        // El listener tendrá la funcionalidad de ir "escuchando" la palabra que TextToSpeech vaya leyendo (reproducionedo) y esta se pintará de otro color
        val listener = object : UtteranceProgressListener() {
            override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                val spannableString = SpannableString(response)
                spannableString.setSpan(
                    Color.parseColor(R.color.app_currently_spoken_text_color.toString()),   // La palabra "hablada" se pintará de este otro color
                    start, end, 0)
                runOnUiThread { binding.tvResponse.text = spannableString }
            }

            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                runOnUiThread { binding.ltRobot.pauseAnimation() }
            }

            override fun onError(utteranceId: String?) {
                runOnUiThread { binding.tvWelcome.text = "" }
            }
        }
        textToSpeech.setOnUtteranceProgressListener(listener)
        textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}