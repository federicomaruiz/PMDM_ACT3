package com.utad.ideas.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.utad.ideas.R
import com.utad.ideas.databinding.ActivityCreateBinding
import com.utad.ideas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var _binding: ActivityMainBinding
    private val binding: ActivityMainBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}