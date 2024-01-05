package com.example.repository.weather.utils

fun Double.toThreeDigits(): Double {
    return "%.3f".format(this).toDouble()
}

