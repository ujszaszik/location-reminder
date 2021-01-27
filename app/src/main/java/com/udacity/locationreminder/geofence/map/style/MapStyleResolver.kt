package com.udacity.locationreminder.geofence.map.style

object MapStyleResolver {

    fun getValue(selectedItemId: Int): MapStyleHolder? {
        var resourceId: Int? = MapTypeSelection.getSelectedMapType(selectedItemId)
        var type = MapStyle.MAP_TYPE
        resourceId.ifNull {
            resourceId = MapThemeSelection.getSelectedMapType(selectedItemId)
            type = MapStyle.MAP_THEME
        }
        return resourceId?.let { MapStyleHolder(it, type) }
    }

}

private fun <T> T.ifNull(block: () -> Unit) {
    if (this == null) block.invoke()
}