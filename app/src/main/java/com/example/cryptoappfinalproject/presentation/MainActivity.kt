package com.example.cryptoappfinalproject.presentation

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.CursorWindow
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.cryptoappfinalproject.NavGraphDirections
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.LangSettings
import com.example.cryptoappfinalproject.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Field
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth

    var internet: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding!!.sideNavigation.itemIconTintList = null

        increaseMemorySize()
        setUpSideNavigation()
        isOnline(this)

        val state = getSharedPreferences(LangSettings.LANG_PREF, 0).getString(LangSettings.LANG_KEY, LangSettings.ENG)
        if (state == LangSettings.ENG) {
            changeLanguage(LangSettings.ENG)
        }
        if (state == LangSettings.GEO) {
            changeLanguage(LangSettings.GEO)


        }
        if (state == LangSettings.AMHARIC) {
            changeLanguage(LangSettings.AMHARIC)

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

        if (firebaseAuth.currentUser != null) {
            menuLogIn.isVisible = false

        }

        if (firebaseAuth.currentUser == null) {
            menuLogIn.isVisible = true
            menuLogIn.setOnMenuItemClickListener {
                navController.navigate(NavGraphDirections.actionToLogIn())
                binding!!.drawer.closeDrawer(Gravity.LEFT)
                true
            }
        }
        if (firebaseAuth.currentUser == null) {
            menuSupport.isVisible = false
        }
        if (firebaseAuth.currentUser != null) {
            menuSupport.isVisible = true

        }

        menuSupport.setOnMenuItemClickListener {
            if (firebaseAuth.currentUser!!.email == "llomi18@freeuni.edu.ge") {
                navController.navigate(NavGraphDirections.actionMenuToChatFragment())
                binding!!.drawer.closeDrawer(Gravity.LEFT)
            } else  {
                navController.navigate(NavGraphDirections.actionMenuToChatActivityFragment("Support"))
                binding!!.drawer.closeDrawer(Gravity.LEFT)
            }


            true
        }

        if (firebaseAuth.currentUser != null) {
            menuSignOut.isVisible = true
        }
        if (firebaseAuth.currentUser == null) {
            menuSignOut.isVisible = false
        }

        menuSignOut.setOnMenuItemClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                this@MainActivity,
                getString(R.string.you_signed_out),
                Toast.LENGTH_LONG
            ).show()
            true

        }

    }


    private fun isOnline(context: Context): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                lifecycleScope.launch(Dispatchers.Main) {
                    if (internet) {
                        navController.navigate(MainActivityDirections.actionGlobal())
                        Toast.makeText(
                            context,
                            getString(R.string.connection_restored),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }


            }


            override fun onLost(network: Network) {
                Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_LONG).show()
                internet = true
            }


        })

        if (connectivityManager != null) {

            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true

                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }

        }
        return true

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


