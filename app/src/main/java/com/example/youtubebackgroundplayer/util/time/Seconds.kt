package com.example.youtubebackgroundplayer.util.time

import com.example.youtubebackgroundplayer.constant.DateTimeConstants
import java.util.concurrent.TimeUnit

inline class Seconds(val seconds: Int): MeasureOfTime {
    override val millis
        get() = seconds.toLong() * DateTimeConstants.MILLIS_IN_SECOND

    override val displayableTime
        get() = String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )

    override fun plus(another: MeasureOfTime): Seconds =
        when(another) {
            is Seconds -> {
                Seconds(this.seconds + another.seconds)
            }
            is Minutes -> {
                Seconds(this.seconds + another.seconds)
            }
            else -> {
                throw IllegalStateException()
            }
        }
}
