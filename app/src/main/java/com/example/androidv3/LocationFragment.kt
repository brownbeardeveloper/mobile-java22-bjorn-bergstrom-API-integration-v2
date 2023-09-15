package com.example.androidv3

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import android.Manifest
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.util.Calendar

class LocationFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitudeView : TextView
    private lateinit var longitudeView : TextView
    private lateinit var locationBtn : Button
    private lateinit var backToMainActivityBtn : TextView

    private var CHANNEL_ID = "androidv3"
    private var CHANNEL_NAME = "channel"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var root = inflater.inflate(R.layout.fragment_location, container, false)

        latitudeView = root.findViewById(R.id.tv_latitude)
        longitudeView = root.findViewById(R.id.tv_longitude)
        locationBtn = root.findViewById(R.id.bt_location)
        backToMainActivityBtn = root.findViewById(R.id.back_to_main_activity_btn)

        initLocationProviderClient()

        locationBtn.setOnClickListener {
            getUserLocation()
            getNotification()
        }

        backToMainActivityBtn.setOnClickListener {
            cleanBackStack()
        }

        return root
    }

    private fun cleanBackStack() {

        val fm: FragmentManager = parentFragmentManager
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    }

    private fun getUserLocation() {

        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)

                // Do something here if we have time

        } else{
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
                var latitude = location?.latitude.toString()
                var longitude = location?.longitude.toString()

                latitudeView.text = latitude
                longitudeView.text = longitude

                sendLocToFirebase(latitude, longitude)
            }
        }
    }

    private fun initLocationProviderClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun sendLocToFirebase(latitude: String, longitude : String){

        var url = "https://api-integration-ebb03-default-rtdb.europe-west1.firebasedatabase.app/.json"

        var rq = Volley.newRequestQueue(context)

        val model = Build.MODEL

        val data = JSONObject()
        data.put("device model", model)
        data.put("device latitude", latitude)
        data.put("device longitude", longitude)

        val re = JsonObjectRequest(Request.Method.POST, url, data,
            { response ->
                // Handle the success response here
                Log.i("LocationFragment", "Data sent successfully: $response")
            },
            { error ->
                // Handle the error here
                Log.e("LocationFragment", "Error sending data: $error")
            })

        rq.add(re)
    }

    private fun getNotification() {

        var notificationBuilder : NotificationCompat.Builder? = null
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        val bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.liam_neeson_found_you)
        val intent = Intent(requireContext(), MainActivity::class.java )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(requireActivity(), Calendar.getInstance().timeInMillis.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)

        val bigPictureNotification = NotificationCompat.BigPictureStyle().bigPicture(bitmap)
        bigPictureNotification.bigPicture(bitmap)

        notificationBuilder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle("Liam Neeson")
            .setContentText("I found you, and I will kill you.")
            .setSmallIcon(R.drawable.translate_icon)
            .setStyle(bigPictureNotification)
            .addAction(R.drawable.liam_neeson_found_you, "Show activity", pendingIntent)

        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(0, notificationBuilder.build())

    }
}