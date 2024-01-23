package kr.co.htap.navigation.location

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import kr.co.htap.navigation.reservation.BranchEntity

/**
 *
 * @author eunku
 */
class LocationProvider(private val context: Context){
   private val REQUEST_LOCATION = 1
    private lateinit var locations : Location

    @RequiresApi(Build.VERSION_CODES.Q)
    private val permissionsLocationUpApi29Impl = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    @TargetApi(Build.VERSION_CODES.P)
    private val permissionsLocationDownApi29Impl = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun requestLocation() {
        if (Build.VERSION.SDK_INT >= 29) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationUpApi29Impl[0]
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationUpApi29Impl[1]
                ) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationUpApi29Impl[2]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissionsLocationUpApi29Impl,
                    REQUEST_LOCATION
                )
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationDownApi29Impl[0]
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationDownApi29Impl[1]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissionsLocationDownApi29Impl,
                    REQUEST_LOCATION
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    public fun getLocation(textView: TextView) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    locations = location
                }
            }
            .addOnFailureListener { fail ->
                textView.text = fail.localizedMessage
            }
    }
    @SuppressLint("MissingPermission")
    public fun getDistance(textView: TextView) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    val testLocal = Location("testPoint")
                    testLocal.apply {
                        latitude = 37.525730
                        longitude = 126.927856
                    }
                    textView.text = "${String.format("%0.1f", location.distanceTo(testLocal) / 1000)}KM"
                }
            }
            .addOnFailureListener { fail ->
                textView.text = fail.localizedMessage
            }
    }
    @SuppressLint("MissingPermission")
    public fun getDistances(branch : BranchEntity) : String {
        val testLocal = Location("testPoint")
        testLocal.apply {
            latitude = branch.latitude
            longitude = branch.longitude
        }

        var response= "${String.format("%0.1f", locations.distanceTo(testLocal) / 1000)}KM"

        return response
    }
}