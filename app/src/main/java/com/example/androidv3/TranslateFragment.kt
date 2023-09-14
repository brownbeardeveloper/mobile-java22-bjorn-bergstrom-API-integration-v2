package com.example.androidv3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslateFragment : Fragment() {

    private lateinit var root : View
    private var items = arrayOf("English","Swedish","French","Spanish")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.i("TranslateFragment", "Fragment onCreateView started")

        root = inflater.inflate(R.layout.fragment_translate, container, false)

        val runTranslateBtn = root.findViewById<Button>(R.id.run_translate_btn)
        val inputTextView = root.findViewById<TextView>(R.id.input)
        val outputTextView = root.findViewById<TextView>(R.id.output)

        val languageFrom = root.findViewById<AutoCompleteTextView>(R.id.language_from)
        val languageTo = root.findViewById<AutoCompleteTextView>(R.id.language_to)

        val itemAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items
        )

        itemAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        languageFrom.setAdapter(itemAdapter)
        languageTo.setAdapter(itemAdapter)

        runTranslateBtn.setOnClickListener {

            Log.i("TranslateFragment", "Input Text: ${inputTextView.text}")
            Log.i("TranslateFragment", "Source Language: ${selectFrom()}")
            Log.i("TranslateFragment", "Target Language: ${selectTo()}")

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(selectFrom())
                .setTargetLanguage(selectTo())
                .build()

            val translator = Translation.getClient(options)

            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()

            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {

                    Log.i("TranslateFragment", "Model downloaded successfully. Okay to start translating.")

            }
                .addOnFailureListener { exception ->
                    Log.e("TranslateFragment", "Model download failed: ${exception.message}", exception)
                    Toast.makeText(MainActivity(),exception.message,Toast.LENGTH_SHORT).show()
                }

            translator.translate(inputTextView.text.toString())
                .addOnSuccessListener { translatedText ->
                    Log.i("TranslateFragment", "Translated Text: $translatedText")
                    outputTextView.text = translatedText
                }
                .addOnFailureListener { exception ->
                    Log.e("TranslateFragment", "Translation failed: ${exception.message}", exception)
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
        }

        return root
    }

    private fun selectFrom(): String {

        val languageFrom = root.findViewById<TextView>(R.id.language_from)

        Log.i("TranslateFragment", "Selected Source Language: ${languageFrom.text.toString()}")

        return when (languageFrom.text.toString()){

            "English"-> TranslateLanguage.ENGLISH
            "Swedish"-> TranslateLanguage.SWEDISH

            else -> TranslateLanguage.ENGLISH
        }
    }

    private fun selectTo(): String {

        val languageTo = root.findViewById<TextView>(R.id.language_to)

        Log.i("TranslateFragment", "Selected Target Language: ${languageTo.text.toString()}")

        return when (languageTo.text.toString()){

            "English"-> TranslateLanguage.ENGLISH
            "Swedish"-> TranslateLanguage.SWEDISH
            "Spanish"-> TranslateLanguage.SPANISH
            "French"-> TranslateLanguage.FRENCH

            else -> TranslateLanguage.ENGLISH
        }
    }
}