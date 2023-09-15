package com.example.androidv3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SynonymFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_synonym, container, false)

        // https://rapidapi.com/dpventures/api/wordsapi/

        // Skipping this part cos I have to use a credit card to get the API key


        return root
    }
}