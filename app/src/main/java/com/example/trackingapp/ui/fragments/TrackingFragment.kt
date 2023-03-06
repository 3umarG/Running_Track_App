package com.example.trackingapp.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentTrackingBinding
import com.example.trackingapp.ui.base.services.PolyLinePoints
import com.example.trackingapp.utils.Constants
import com.example.trackingapp.ui.base.services.TrackingService
import com.example.trackingapp.ui.viewmodels.MainViewModel
import com.example.trackingapp.utils.Constants.CAMERA_ZOOM
import com.example.trackingapp.utils.Constants.POLY_LINE_COLOR
import com.example.trackingapp.utils.Constants.POLY_LINE_WIDTH
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {
    private lateinit var binding: FragmentTrackingBinding
    private val viewModel: MainViewModel by viewModels()
    private var map: GoogleMap? = null


    private var isTracking = false
    private var pathPoints = mutableListOf<PolyLinePoints>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
        }

        binding.btnToggleRun.setOnClickListener {
            sendCommandToService(Constants.SERVICE_ACTION_START_OR_RESUME)
        }
    }


    /**
     * That Connect the last two points on the polyline and draw it on the map ...
     */
    private fun addLatestPolyLine() {
        // I should check that the last line has at least two points to connect them.
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            // Start to get the LatLng of the last and preLast points.
            val lastLatLng = pathPoints.last().last()
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2];

            // Define how the polyline will look like on the map.
            val polylineOptions = PolylineOptions()
                .color(POLY_LINE_COLOR)
                .width(POLY_LINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)


            // Put this polyline in the map
            map?.addPolyline(polylineOptions)

        }
    }


    /**
     *That will used in case where the screen rotated and lost all the previous path points.
     */
    private fun addAllPolyLines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLY_LINE_COLOR)
                .width(POLY_LINE_WIDTH)
                .addAll(polyline)

            map?.addPolyline(polylineOptions)
        }
    }

    /**
     * That will Move the Camera zoom to the last movement position.
     */
    private fun moveCameraZoomToLastPos() {
        // I wil check for there is at least one movement
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    CAMERA_ZOOM
                )
            )
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}