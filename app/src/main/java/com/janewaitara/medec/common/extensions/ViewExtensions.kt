package com.janewaitara.medec.common.extensions

import android.view.View

fun View.onClick(onClickAction: () -> Unit) {
    setOnClickListener { onClickAction() }
}

fun View.isVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}