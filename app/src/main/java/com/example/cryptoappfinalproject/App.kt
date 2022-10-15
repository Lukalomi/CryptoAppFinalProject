package com.example.cryptoappfinalproject

import android.app.Application
import android.support.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: MultiDexApplication() {
}