package com.example.androidv3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class HomeFragment : Fragment() {

    private lateinit var mainBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        mainBtn = root.findViewById(R.id.main_fragment_btn)

        mainBtn.setOnClickListener {

            var fm : FragmentManager = parentFragmentManager
            var ft : FragmentTransaction = fm.beginTransaction()

            ft.replace(R.id.nav_host_fragment, LocationFragment(), "location")
                .addToBackStack("9")
            ft.commit()

        }

        return root
    }
}