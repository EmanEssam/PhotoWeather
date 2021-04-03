package com.test.photoweather.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.test.photoweather.R
import com.test.photoweather.data.model.WeatherResponse
import com.test.photoweather.databinding.ActivityMainBinding
import com.test.photoweather.ui.base.BaseActivity
import com.test.photoweather.ui.viewmodel.MainViewModel
import com.test.photoweather.utils.PhotoProcessing
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override val mViewModel: MainViewModel by viewModels()
    private var result: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getLocationPermission()
        takePhotoBtn.setOnClickListener {
            getContent.launch(null)
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation(result: Bitmap) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                mViewModel.getWeather(
                    location?.latitude.toString(),
                    location?.longitude.toString()
                )
                    .observe(this, {
                        handleWeatherResponse(it,result)

                    })

            }
    }
    private fun handleWeatherResponse(it: WeatherResponse, result: Bitmap) {
        val captionString =
            "City Name:\n ${it.name}  \n Weather: \n ${it.weather[0].description}"
        val image =
            PhotoProcessing.drawTextToBitmap(
                this,
                result,
                captionString
            )
        myImg.visibility = View.VISIBLE
        myImg.setImageBitmap(
            image
        )
        loader.visibility = View.GONE
        takePhotoBtn.visibility = View.VISIBLE
        showShareButton(image!!)
    }

    private fun showShareButton(image: Bitmap) {
        shareBtn.visibility = View.VISIBLE
        val photo = SharePhoto.Builder().setBitmap(image).build()
        val content =
            SharePhotoContent.Builder().addPhoto(photo).build()
        shareBtn.shareContent = content
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    private val getContent =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
            myImg.visibility = View.GONE
            loader.visibility = View.VISIBLE
            takePhotoBtn.visibility = View.GONE
            getUserLocation(result)
        }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PERMISSION_GRANTED)
                    ) {
                        if (result != null)
                            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    takePhotoBtn.visibility = View.GONE
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}