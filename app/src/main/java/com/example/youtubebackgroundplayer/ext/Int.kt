package com.example.youtubebackgroundplayer.ext

fun Int.minusOneToNull() =
    if (this == -1) { null } else { this }