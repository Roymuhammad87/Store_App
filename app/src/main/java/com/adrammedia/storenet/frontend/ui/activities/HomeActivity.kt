package com.adrammedia.storenet.frontend.ui.activities


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

import androidx.lifecycle.lifecycleScope
import com.adrammedia.storenet.R
import com.adrammedia.storenet.databinding.ActivityHomeBinding
import com.adrammedia.storenet.frontend.ui.fragments.HomeFragment
import com.adrammedia.storenet.frontend.ui.fragments.MyToolsFragment
import com.adrammedia.storenet.frontend.ui.fragments.ProfileFragment
import com.adrammedia.storenet.frontend.viewmodels.LogoutViewModel
import com.adrammedia.storenet.utils.Constants
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val logoutViewModel by viewModels<LogoutViewModel>()


    companion object {
        var apiToken =""
        var userId = ""
        var longitude = ""
        var latitude = ""
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)
        sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        apiToken = sharedPreferences.getString("userToken", "").toString()
        userId = sharedPreferences.getString("id", "").toString()
        getGeo()
        longitude = sharedPreferences.getString("longitude","01.0000000001").toString()
        latitude = sharedPreferences.getString("latitude","01.0000000001").toString()



        binding.toolbarHome.apply {
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.logout_option->logout()
                }
                true
            }
        }


        navigate()

        onBackPressedDispatcher.addCallback(this, object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (binding.bottomNavigationView.selectedItemId == R.id.home_option){
                    finish()
                } else {
                    binding.bottomNavigationView.selectedItemId =R.id.home_option
                }
            }
        })

    }

    private fun logout() {
        logoutViewModel.logout()
        lifecycleScope.launch {
            logoutViewModel.logoutStateFlow.collect {
                it?.status.let { status ->
                    if (status == 200) {
                        sharedPreferences.edit().clear().apply()
                        Toast.makeText(this@HomeActivity, it?.message, Toast.LENGTH_SHORT).show()
                        Intent(this@HomeActivity, LoginActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun navigate() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_option -> replaceFragment(HomeFragment())
                R.id.my_tools_option -> replaceFragment(MyToolsFragment())
                R.id.profile_option -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
         val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainerView.id, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getGeo() {
        if (checkLocationPermission()){
            if (isLocationServicesAvailable()) {
                if(isLocationEnabled()){
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) {
                        val location = it.result
                        if (location == null){
                            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
                        } else {
                            editor.putString("longitude", location.longitude.toString())
                            editor.putString("latitude", location.latitude.toString())
                            editor.apply()
                            Log.d("TAG", "lak:long:$longitude, lat:${latitude}")
                        }
                    }
                } else {
                    //open setting
                    Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

            } else {
                Toast.makeText(this, "Location services not available", Toast.LENGTH_SHORT).show()
            }

        } else {
            requestLocationPermission()
        }
    }


    private fun checkLocationPermission():Boolean {
        return (ActivityCompat.checkSelfPermission(this,
            Constants.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
            Constants.ACCESS_LOCATION) == PackageManager.PERMISSION_GRANTED
         )
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkMemoryPermissions():Boolean{
        return        (ActivityCompat.checkSelfPermission(this,
            Constants.STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
            Constants.AUDIO_PERMISSION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
            Constants.VIDEO_PERMISSION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
            Constants.WRITE_PERMISSION) == PackageManager.PERMISSION_GRANTED)
    }


    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(this,
           arrayOf(
               Constants.ACCESS_LOCATION, Constants.ACCESS_COARSE_LOCATION
           ), 10
        )

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestMemoryPermission(){
        ActivityCompat.requestPermissions(this,
            Constants.PERMISSIONS_ARRAY, 100
        )

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 10) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                if (!checkMemoryPermissions())
                    requestMemoryPermission()
                Toast.makeText(this, "Location Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show()
                if (!checkMemoryPermissions())
                    requestMemoryPermission()
            }
        }
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Memory Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Memory Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun isLocationEnabled():Boolean {
        val locationManger: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManger.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun isLocationServicesAvailable(): Boolean {
        val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return result == ConnectionResult.SUCCESS
    }

}