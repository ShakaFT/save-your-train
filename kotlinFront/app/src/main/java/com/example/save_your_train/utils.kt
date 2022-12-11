package com.example.save_your_train

import android.content.Context
import androidx.appcompat.app.AlertDialog


fun getPopup(context: Context, title: String, content: String,
             positiveFunction: () -> Unit = {}, negativeFunction: () -> Unit = {}): AlertDialog.Builder {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(content)

    builder.setPositiveButton(android.R.string.ok) { _, _ ->
        positiveFunction()
    }

    builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        negativeFunction()
    }

    return builder
}