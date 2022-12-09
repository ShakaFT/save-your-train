package com.example.save_your_train

import android.view.View
import android.widget.Button
import android.widget.TextView

fun disableButton(button: Button, disable: Boolean) {
    button.isClickable = !disable
    button.alpha = if(disable) 0.3F else 1F
}

fun displayTextView(textView: TextView, display: Boolean) {
    textView.visibility = if (display) View.VISIBLE else View.GONE
}