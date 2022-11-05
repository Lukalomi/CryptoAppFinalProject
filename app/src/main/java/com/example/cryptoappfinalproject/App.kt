package com.example.cryptoappfinalproject

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.example.cryptoappfinalproject.common.DayNightModeSettings
import com.example.cryptoappfinalproject.presentation.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        appContext = this
        setScreenMode()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FirebaseMessaging", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FirebaseMessaging", "$token")
        })

        val intent = Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)

        isOnline(appContext)
    }


    companion object {

        lateinit var appContext: Application
    }

    private fun setScreenMode() {
        val state = getSharedPreferences(DayNightModeSettings.SCREEN_PREF, 0).getBoolean(DayNightModeSettings.SCREEN_KEY, DayNightModeSettings.IS_TRUE)

        if (state) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            //when dark mode is enabled, we use the dark theme
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    private fun isOnline(context: Context,): Boolean {



        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)


        if (connectivityManager != null) {

            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true

                }
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }

        }
        if (capabilities == null) {
            Log.i("Internet", "no int")
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
        }

        return false
    }




}