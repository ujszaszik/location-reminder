package com.udacity.locationreminder.geofence.map.style

import com.udacity.locationreminder.R

enum class MapThemeSelection(
    private val menuItemId: Int,
    private val mapThemeResourceId: Int
) {
    AUBERGINE(R.id.aubergineMapType, R.raw.map_aubergine_theme),
    BLUE_BIRD(R.id.blueBirdMapType, R.raw.map_bluebird_theme),
    DARK(R.id.darkMapType, R.raw.map_dark_theme),
    NIGHT(R.id.nightMapType, R.raw.map_night_theme),
    RETRO(R.id.retroMapType, R.raw.map_retro_theme),
    SILVER(R.id.silverMapType, R.raw.map_silver_theme);

    companion object {

        fun getSelectedMapType(selectedMenuItemId: Int): Int? {
            return values()
                .filter { selectedMenuItemId == it.menuItemId }
                .map { it.mapThemeResourceId }
                .firstOrNull()
        }
    }
}