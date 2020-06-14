package com.example.youtubebackgroundplayer.util.time

import com.example.youtubebackgroundplayer.constant.DateTimeConstants
import java.util.concurrent.TimeUnit
import kotlin.math.floor

inline class Minutes(val minutes: Int):
    MeasureOfTime {
    override val millis
        get() = minutes.toLong() * DateTimeConstants.SECONDS_IN_MINUTE * DateTimeConstants.MILLIS_IN_SECOND

    override val displayableTime
        get() = String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
        )

    val seconds
        get() = minutes * DateTimeConstants.SECONDS_IN_MINUTE

    override fun plus(another: MeasureOfTime): Minutes =
        when(another) {
            is Seconds -> {
                Minutes(
                    (this.minutes + floor(
                        another.seconds.toDouble() / DateTimeConstants.SECONDS_IN_MINUTE
                    )).toInt()
                )
            }
            is Minutes -> {
                Minutes(this.minutes + another.minutes)
            }
            else -> {
                throw IllegalStateException()
            }
        }
}