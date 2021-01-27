package com.udacity.locationreminder.reminders

class ItemClickListener<T>(private val block: (T) -> Unit) {

    fun onClick(param: T) {
        block.invoke(param)
    }
}