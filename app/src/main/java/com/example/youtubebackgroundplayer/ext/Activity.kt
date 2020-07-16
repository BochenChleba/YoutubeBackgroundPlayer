package com.example.youtubebackgroundplayer.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

@Suppress("UNCHECKED_CAST")
fun <T: Fragment> AppCompatActivity.getFragment(id: Int) =
    supportFragmentManager
        .findFragmentById(id)
        ?.let { it as? T }
