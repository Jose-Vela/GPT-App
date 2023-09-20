package com.example.gptapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gptapp.databinding.AssistantActivityBinding
import java.util.Locale

class AssistantActivity : AppCompatActivity() {

    private lateinit var binding: AssistantActivityBinding

    private val startActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == Activity.RESULT_OK) {
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
            askSpeechInput()
        }
    }

    private fun askSpeechInput() {
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

}