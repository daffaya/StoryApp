package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.home.HomeFragment
import com.example.storyapp.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val botNav: BottomNavigationView = binding.botNav

        // Here, you can get your stored token & userId from Preferences/DataStore
        // If there is any, redirect to HomeFragment
        // Else, redirect to LoginActivity

        val sharedPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val storedToken = sharedPrefs.getString("token", null)
        val storedUserId = sharedPrefs.getInt("userId", -1)

        if (storedToken != null && storedUserId != -1) {
            val fragmentManager = supportFragmentManager
            val homeFragment = HomeFragment()
            val fragment = fragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

            if (fragment !is HomeFragment) {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragment_home_nav) as NavHostFragment
                val navController = navHostFragment.navController
                botNav.setupWithNavController(navController)
            }
            // Redirect to HomeFragment
            // ...
        } else {
            // Redirect to LoginActivity
            binding.btnKesana.setOnClickListener{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}