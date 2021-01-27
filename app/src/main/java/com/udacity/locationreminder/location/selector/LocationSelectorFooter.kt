package com.udacity.locationreminder.location.selector

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.udacity.locationreminder.databinding.LocationSelectorFooterBinding
import com.udacity.locationreminder.geofence.map.SeekBarProgressChangedListener

class LocationSelectorFooter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: LocationSelectorFooterBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = LocationSelectorFooterBinding.inflate(inflater, this, true)
        setSeekBarToInitialPosition()
        binding.saveButton.isEnabled = false
    }

    fun setSeekBarChangedListener(listener: (Int) -> Unit) {
        binding.radiusSeekBar.setOnSeekBarChangeListener(
            SeekBarProgressChangedListener { listener.invoke(it) }
        )
    }

    fun setOnCancelClicked(listener: () -> Unit) {
        binding.cancelButton.setOnClickListener { listener.invoke() }
    }

    fun setOnSaveClicked(listener: () -> Unit) {
        binding.saveButton.setOnClickListener { listener.invoke() }
    }

    private fun setSeekBarToInitialPosition() {
        binding.radiusSeekBar.isEnabled = false
        binding.minText.setTextColor(Color.GRAY)
        binding.maxText.setTextColor(Color.GRAY)
    }

    fun onEnabled() {
        enableSeekBar()
        enableSaving()
    }

    private fun enableSeekBar() {
        binding.radiusSeekBar.isEnabled = true
        binding.minText.setTextColor(Color.WHITE)
        binding.maxText.setTextColor(Color.WHITE)
    }

    fun resetSeekBar() {
        binding.radiusSeekBar.progress = 0
    }

    private fun enableSaving() {
        binding.saveButton.isEnabled = true
    }
}