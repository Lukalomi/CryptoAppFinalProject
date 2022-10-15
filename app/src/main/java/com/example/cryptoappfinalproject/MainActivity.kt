package com.example.cryptoappfinalproject

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.cryptoappfinalproject.databinding.ActivityMainBinding
import com.example.cryptoappfinalproject.ui.registration.RegistrationViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.sideNavigation.itemIconTintList = null

        setUpSideNavigation()
        populateProfilePicture()
    }


    private fun setUpSideNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navigationView: NavigationView = binding!!.sideNavigation
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    private fun populateProfilePicture() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.readAllUserInfo().collect{
                val profilePicture = findViewById<ImageView>(R.id.ivUserPhoto)
                it.forEach {
                    profilePicture.setImageResource(it.image.toInt())
                }
            }
        }
    }


}


