package com.udacity.locationreminder.location.selector

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.udacity.locationreminder.R

class LocationSelectorLegend @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    init {
        inflate(getContext(), R.layout.location_selector_legend, this);
    }
}