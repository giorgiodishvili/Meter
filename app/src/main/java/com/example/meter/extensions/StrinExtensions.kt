package com.example.meter.extensions

fun String.isEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isNotEmail(): Boolean {
    return !(android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches())
}