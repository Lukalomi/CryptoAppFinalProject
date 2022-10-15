package com.example.cryptoappfinalproject

import android.database.CursorWindow
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.databinding.ActivityMainBinding
import com.example.cryptoappfinalproject.ui.registration.RegistrationViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Field


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

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
                    Glide.with(this@MainActivity)
                        .load(it.image)
                        .error(R.drawable.ic_launcher_background)
                        .into(profilePicture)
                }
            }
        }
    }


}


