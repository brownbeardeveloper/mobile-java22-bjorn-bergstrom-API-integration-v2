package com.example.androidv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.androidv3.databinding.ActivityMainBinding

class
MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.home -> {
                    var fm : FragmentManager = supportFragmentManager
                    var ft : FragmentTransaction = fm.beginTransaction()

                    ft.replace(R.id.nav_host_fragment, HomeFragment(), "home")
                        .addToBackStack("1")
                    ft.commit()

                    return@setOnItemSelectedListener true
                }
                R.id.translate -> {
                    var fm : FragmentManager = supportFragmentManager
                    var ft : FragmentTransaction = fm.beginTransaction()

                    ft.replace(R.id.nav_host_fragment, TranslateFragment(), "translate")
                        .addToBackStack("2")
                    ft.commit()

                    return@setOnItemSelectedListener true
                }
                R.id.synonym -> {
                    var fm : FragmentManager = supportFragmentManager
                    var ft : FragmentTransaction = fm.beginTransaction()

                    ft.replace(R.id.nav_host_fragment, SynonymFragment(), "synonym")
                        .addToBackStack("3")
                    ft.commit()
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }
}