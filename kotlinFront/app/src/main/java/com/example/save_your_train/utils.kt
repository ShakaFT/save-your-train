package com.example.save_your_train

import android.widget.Button

fun disableButton(button: Button, disable: Boolean) {
    button.isClickable = !disable
    button.alpha = if(disable) 0.3F else 1F
}