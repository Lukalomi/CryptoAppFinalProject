package com.example.cryptoappfinalproject

import android.content.Intent
import android.database.CursorWindow
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.cryptoappfinalproject.databinding.ActivityMainBinding

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding!!.sideNavigation.itemIconTintList = null
        increaseMemorySize()
        setUpSideNavigation()
//        setScreenMode()





    }


    private fun setScreenMode() {
        val state = getSharedPreferences("AppSettingPrefs", 0).getBoolean("DayMode", true)

        if (state) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            //when dark mode is enabled, we use the dark theme
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setUpSideNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navigationView: NavigationView = binding!!.sideNavigation
        NavigationUI.setupWithNavController(navigationView, navController)

        val menuSignOut = navigationView.menu.getItem(3)

        menuSignOut.setOnMenuItemClickListener {
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@MainActivity, "You Signed Out", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "You are already signed out", Toast.LENGTH_LONG)
                    .show()
            }
            true

        }

    }


    private fun increaseMemorySize() {
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}


