package com.planatech.instabugtask.main.repository

import java.net.URL
import javax.inject.Inject

class MainRepository @Inject constructor() {
    fun loadWebsite(url: String): String {
        return URL("https://instabug.com").readText()
    }
}