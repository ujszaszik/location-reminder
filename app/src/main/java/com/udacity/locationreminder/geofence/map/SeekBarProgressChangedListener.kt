package com.udacity.locationreminder.geofence.map

import android.widget.SeekBar

class SeekBarProgressChangedListener(private val block: (Int) -> Unit) :
    SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, newValue: Int, fromUser: Boolean) {
        block.invoke(newValue)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}