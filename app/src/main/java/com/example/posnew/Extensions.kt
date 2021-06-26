package com.example.posnew

import android.util.Patterns

fun CharSequence?.isEmailValid() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

