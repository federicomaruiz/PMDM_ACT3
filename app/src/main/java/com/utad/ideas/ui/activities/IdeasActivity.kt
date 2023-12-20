package com.utad.ideas.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.utad.ideas.R
import com.utad.ideas.databinding.ActivityIdeasBinding

class IdeasActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityIdeasBinding
    private val binding: ActivityIdeasBinding get() = _binding

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityIdeasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavigation()
        listenFloating()

    }



    private fun listenFloating() {
        binding.btnFloating.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setBottomNavigation() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(binding.nhfView.id) as NavHostFragment
        navController = navHostFragment.findNavController()

        /*
               if (navHostFragment != null) {
                   binding.bnvNews.setupWithNavController(navController)
               } */

        navController.navigate(R.id.ideasListFragment)

        binding.bnvNews.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ideasListFragment -> {
                    navController.navigate(R.id.ideasListFragment)
                    true
                }

                R.id.userDataFragment -> {
                    navController.navigate(R.id.userDataFragment)
                    true
                }

                else -> false
            }
        }




    }
}