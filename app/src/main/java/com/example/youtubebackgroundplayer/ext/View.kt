package com.example.youtubebackgroundplayer.ext

import android.view.View

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.showIf(expr: Boolean) {
    if (expr) {
        show()
    } else {
        hide()
    }
}
