package com.example.cryptoappfinalproject.presentation

import android.content.Intent
import android.content.res.Configuration
import android.database.CursorWindow
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.cryptoappfinalproject.NavGraphDirections
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.databinding.ActivityMainBinding

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding!!.sideNavigation.itemIconTintList = null
        increaseMemorySize()
        setUpSideNavigation()

        val state = getSharedPreferences("languagePref", 0).getString("Language", "en")
        if (state == "en") {
            changeLanguage("en")
        }
        if (state == "ge") {
            changeLanguage("ge")

        }
        if (state == "amharic") {
            changeLanguage("am")

        }


    }

    private fun changeLanguage(language: String?) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(
            config,
            resources.displayMetrics
        )
    }

    private fun setUpSideNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navigationView: NavigationView = binding!!.sideNavigation
        NavigationUI.setupWithNavController(navigationView, navController)

        val menuSignOut = navigationView.menu.getItem(4)
        val menuSupport = navigationView.menu.getItem(3)
        val menuLogIn = navigationView.menu.getItem(0)

        if(firebaseAuth.currentUser != null) {
            menuLogIn.isVisible = false

        }

        if(firebaseAuth.currentUser == null ) {
            menuLogIn.isVisible = true
            menuLogIn.setOnMenuItemClickListener {
                navController.navigate(NavGraphDirections.actionToLogIn())
                binding!!.drawer.closeDrawer(Gravity.LEFT)
                true
            }
        }
        if(firebaseAuth.currentUser == null) {
            menuSupport.isVisible = false
        }
        if(firebaseAuth.currentUser != null) {
            menuSupport.isVisible = true

        }

        menuSupport.setOnMenuItemClickListener {
            if (firebaseAuth.currentUser!!.email == "llomi18@freeuni.edu.ge") {
                navController.navigate(NavGraphDirections.actionMenuToChatFragment())
                binding!!.drawer.closeDrawer(Gravity.LEFT)
            } else if (firebaseAuth.currentUser!!.email != "llomi18@freeuni.edu.ge") {
                navController.navigate(NavGraphDirections.actionMenuToChatActivityFragment("Support"))
                binding!!.drawer.closeDrawer(Gravity.LEFT)
            }


            true
        }

        menuSignOut.setOnMenuItemClickListener {
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.you_signed_out),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.already_signed_out),
                    Toast.LENGTH_LONG
                )
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


