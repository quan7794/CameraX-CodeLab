package com.samsung.android.plugin.tv.v3.edgeBlending.util

import android.view.View
import androidx.appcompat.widget.SearchView

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun View.Gone(){
    visibility = View.GONE
}
fun View.Visible() {
    visibility = View.VISIBLE
}

infix fun View.onClick( func : () -> Unit){
    this.setOnClickListener{ func }
}

fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {

    val query = MutableStateFlow("")

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })

    return query

}