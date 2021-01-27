package com.udacity.locationreminder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.udacity.locationreminder.location.LocationSettingsUtil
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
    }

    @KoinApiExtension
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val locationSettingsUtil: LocationSettingsUtil by inject { parametersOf(this) }
        if (requestCode == locationSettingsUtil.requestCode) {
            locationSettingsUtil.checkLocationSettings(false)
        }
    }
}