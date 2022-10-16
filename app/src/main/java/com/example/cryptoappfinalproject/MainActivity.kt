package com.example.cryptoappfinalproject

import android.content.Intent
import android.database.CursorWindow
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.databinding.ActivityMainBinding
import com.example.cryptoappfinalproject.ui.home.HomeFragment
import com.example.cryptoappfinalproject.ui.registration.RegistrationFragment
import com.example.cryptoappfinalproject.ui.registration.RegistrationViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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


    }


    private fun setUpSideNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navigationView: NavigationView = binding!!.sideNavigation
        NavigationUI.setupWithNavController(navigationView, navController)

        val menuSignOut = navigationView.menu.getItem(4)

        val menuLogReg = navigationView.menu.getItem(0)
        menuSignOut.setOnMenuItemClickListener {
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.signOut()
                val intent = Intent(this,MainActivity::class.java)
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


