package com.example.youtubebackgroundplayer.ext

import android.view.View
import android.widget.CheckBox

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

fun CheckBox.addFadeAnimation() =
    setOnClickListener {
        if (isChecked) {
            animate()
                .alpha(1f)
                .setDuration(250)
                .start()
        } else {
            animate()
                .alpha(0.65f)
                .setDuration(250)
                .start()
        }
    }

fun CheckBox.fadeWhenUnchecked() {
    if (!isChecked) {
        alpha = 0.7f
    }
}
