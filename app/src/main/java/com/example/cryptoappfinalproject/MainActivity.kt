package com.example.cryptoappfinalproject

import android.content.Intent
import android.database.CursorWindow
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.databinding.ActivityMainBinding
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
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding!!.sideNavigation.itemIconTintList = null
        increaseMemorySize()
        setUpSideNavigation()
//        populateProfilePicture()


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
                Toast.makeText(this@MainActivity, "You Signed Out", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "You are already signed out", Toast.LENGTH_LONG)
                    .show()
            }
            true

        }

    }


//    private fun populateProfilePicture() {
//
//        lifecycleScope.launch {
//            viewModel.readAllUserInfo().collect {
//                val profilePicture = findViewById<ImageView>(R.id.ivUserPhoto)
//                if(firebaseAuth.currentUser != null){
//                    it.forEach {
//                        Glide.with(this@MainActivity)
//                            .load(it.image)
//                            .error(R.drawable.ic_launcher_background)
//                            .into(profilePicture)
//                    }
//                }
//                else {
//                    Glide.with(this@MainActivity)
//                        .load(R.drawable.ic_person)
//                        .error(R.drawable.ic_launcher_background)
//                        .into(profilePicture)
//                }
//
//            }
//        }
//    }

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


