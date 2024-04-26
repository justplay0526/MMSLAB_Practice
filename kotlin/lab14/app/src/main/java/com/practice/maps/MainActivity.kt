package com.practice.maps

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.practice.maps.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty() && requestCode == 0){
            for (result in grantResults)
                if (result != PackageManager.PERMISSION_GRANTED)
                    finish()    //若拒絕給予定位權限，則關閉應用程式
            loadMap()           //因允許定位權限，所以正常執行應用程式
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadMap()
    }

    override fun onMapReady(map: GoogleMap) {
        if ( ActivityCompat.checkSelfPermission(
                this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else{
            map.isMyLocationEnabled = true
            //加入標記
            val marker = MarkerOptions()
            marker.position(LatLng(25.033611, 121.565000))
            marker.title("台北101")
            marker.draggable(true)
            map.addMarker(marker)
            marker.position(LatLng(25.047924, 121.517081))
            marker.title("台北車站")
            marker.draggable(true)
            map.addMarker(marker)
            //繪製線段
            val polylineOpt = PolylineOptions()
            polylineOpt.add(LatLng(25.033611, 121.565000))
            polylineOpt.add(LatLng(25.032435, 121.534905))
            polylineOpt.add(LatLng(25.047924, 121.517081))
            polylineOpt.color(Color.BLUE)
            val polyline = map.addPolyline(polylineOpt)
            polyline.width = 10f
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(25.035, 121.54), 13f
            ))
        }
    }

    private fun loadMap(){
        val map = supportFragmentManager.findFragmentById(R.id.maps)
            as SupportMapFragment
        map.getMapAsync(this)
    }
}