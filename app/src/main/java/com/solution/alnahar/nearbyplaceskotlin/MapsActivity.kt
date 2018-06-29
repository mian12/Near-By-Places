package com.solution.alnahar.nearbyplaceskotlin

import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.LocationServices
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.solution.alnahar.nearbyplaceskotlin.common.Common
import com.solution.alnahar.nearbyplaceskotlin.model.MyPlaces
import com.solution.alnahar.nearbyplaceskotlin.remote.IGoogleApiService
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    lateinit var mLastLocation: Location
    private var mMarker: Marker? = null

    // location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback


    companion object {
        public val MY_PERMISSION_CODE: Int = 1000

    }


    lateinit var  mServices:IGoogleApiService
          var  currentPlace:MyPlaces?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        // init service
        mServices=Common.googleApiService

        // request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallBack()
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            }
        } else {
            buildLocationRequest()
            buildLocationCallBack()
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }



        bottom_navigation_view.setOnNavigationItemSelectedListener { item: MenuItem ->


            when (item.itemId) {
                R.id.action_market -> nearByPlaces("market")
                R.id.action_school -> nearByPlaces("school")
                R.id.action_resturant -> nearByPlaces("restaurant")
                R.id.action_hospital -> nearByPlaces("hospital")


            }
            true
        }


    }

    private fun nearByPlaces(typePlace: String) {

        // clear all marker on map
        mMap.clear()
        // BUILd url  request  base on location
        val  url=getUrl(latitude,longitude,typePlace)



mServices.getNearByPlaces(url)
        .enqueue(object :Callback<MyPlaces>{
            override fun onFailure(call: Call<MyPlaces>?, t: Throwable?) {

                Toast.makeText(baseContext,""+t!!.message,Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<MyPlaces>?, response: Response<MyPlaces>?) {

                currentPlace=response!!.body()

                if (response!!.isSuccessful)
                {

                    //currentPlace=response.body()

                    for (i in 0 until  response.body()!!.results!!.size)
                    {

                        val markerOptions=MarkerOptions()

                        val  googlePlaces=response.body()!!.results!![i]
                        val lat= googlePlaces.geometry!!.location!!.lat
                        val lng= googlePlaces.geometry!!.location!!.lng

                        val placeName=googlePlaces.name
                        val latLng=LatLng(lat!!.toDouble(),lng!!.toDouble())

                        markerOptions.position(latLng)
                        markerOptions.title(placeName)


                        if (typePlace.equals("hospital"))
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hospital))
                        else if(typePlace.equals("restaurant"))
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_resturant))
                        else if(typePlace.equals("school"))
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school))
                        else if (typePlace.equals("market"))
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_market))
                        else
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                        markerOptions.snippet(i.toString()) // assign index for market
                        // add  marker to map
                        mMap.addMarker(markerOptions)
                        // move camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(17f))



                    }




                }
            }


        })

    }

    private fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {

        var googlePlaceUrl=StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=500")
        googlePlaceUrl.append("&type=$typePlace")
        googlePlaceUrl.append("&key=AIzaSyCuxqtUJ8ZHTZLv-IIumRR15Xa3zWN2gA0")

        Log.e("URL_DEBUG",googlePlaceUrl.toString())

        return  googlePlaceUrl.toString()

    }

    private fun buildLocationRequest() {

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000;
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f


    }

    private fun buildLocationCallBack() {

        locationCallback = object : LocationCallback() {
            //ctrl+o
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations.get(p0.locations.size - 1) // get last location

                if (mMarker != null) {
                    mMarker!!.remove()
                }
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude, longitude)
                val markerOption = MarkerOptions()
                        .position(latLng)
                        .title("Current location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mMarker = mMap.addMarker(markerOption)

                // now move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))

            }
        }

    }


    private fun checkLocationPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_CODE)
            else
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_CODE)
            return false

        } else
            return true


    }

    // ctrl+o

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED)
                        if (checkLocationPermission()) {
                            buildLocationRequest()
                            buildLocationCallBack()
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

                            mMap.isMyLocationEnabled = true
                        }

                } else {
                    Toast.makeText(this, "Permission denied!!", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    override fun onStop() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback)

        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // iniit google play service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true

            }
        } else {
            mMap.isMyLocationEnabled = true
            // enable zoom camera
            mMap.uiSettings.isZoomControlsEnabled = true


        }
        mMap.setOnMarkerClickListener {  marker ->

        // when user select marker,just get result of place  and  assign  to static value

            Common.currentResult=currentPlace!!.results!![Integer.parseInt(marker.snippet)]
            // start new activity
            startActivity(Intent(this,ViewPlaceActivity::class.java))
            true
        }

    }
}
