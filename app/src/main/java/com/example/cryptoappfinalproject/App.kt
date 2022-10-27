package com.example.cryptoappfinalproject

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
<<<<<<< HEAD
import com.example.cryptoappfinalproject.presentation.MainActivity
=======
import com.example.cryptoappfinalproject.ui.favorites.favList
>>>>>>> 8ac69df4bc609d982b9370e492c377fe69238b5a
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import java.util.*


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

        val intent = Intent(this,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)

    }


    companion object {

        lateinit var appContext: Application
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



}