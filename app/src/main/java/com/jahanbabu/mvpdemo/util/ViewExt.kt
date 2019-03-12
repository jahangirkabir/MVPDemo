package com.jahanbabu.mvpdemo.util

import android.content.Context
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.Toast

fun View.showSnackBar(message: String, duration: Int) {
    Snackbar.make(this, message, duration).show()
}

fun View.showToast(context: Context, message: String, duration: Int) {
    Toast.makeText(context, message, duration).show()
}