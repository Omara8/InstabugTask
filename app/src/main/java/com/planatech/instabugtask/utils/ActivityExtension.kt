package com.planatech.instabugtask.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.checkNetworkConnection(context: Context): Boolean {
    val nInfo =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.activeNetworkInfo
    return nInfo != null && nInfo.isAvailable && nInfo.isConnected
}