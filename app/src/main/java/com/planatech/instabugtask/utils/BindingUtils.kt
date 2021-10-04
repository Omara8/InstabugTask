package com.planatech.instabugtask.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("textValue")
fun MaterialTextView.textValue(word: String?) {
    this.text = word
}

@BindingAdapter("intValue")
fun MaterialTextView.intValue(frequency: String?) {
    this.text = frequency
}