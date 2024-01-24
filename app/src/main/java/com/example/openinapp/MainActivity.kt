package com.example.openinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.openinapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navMain.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.link_btn -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.FC.id, LinkFragment())
                        .commit()
                    true
                }
//                R.id.courses_btn -> {}
//                R.id.campaign_btn -> {}
//                R.id.profile_btn -> {}
                else -> {
                    false
                }
            }
        }

    }
}