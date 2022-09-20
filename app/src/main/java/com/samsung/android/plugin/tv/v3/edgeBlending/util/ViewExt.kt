package com.samsung.android.plugin.tv.v3.edgeBlending.util

import android.view.View

fun View.Gone(){
    visibility = View.GONE
}
fun View.Visible() {
    visibility = View.VISIBLE
}

infix fun View.onClick( func : () -> Unit){
    this.setOnClickListener{ func }
}