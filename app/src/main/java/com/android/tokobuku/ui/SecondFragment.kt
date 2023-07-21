package com.android.tokobuku.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.tokobuku.R
import com.android.tokobuku.application.BookApp
import com.android.tokobuku.databinding.FragmentSecondBinding
import com.android.tokobuku.model.Book
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private  val  bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((applicationContext as BookApp).repository)
    }
    private val args: SecondFragmentArgs by navArgs()
    private var book: Book? = null
    private lateinit var mMap: GoogleMap
    private var currentLatLang : LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        book = args.book
        if (book != null){
            binding.button.visibility = View.VISIBLE
            binding.saveButton.text = "ubah"
            binding.nameEditText.setText(book?.name)
            binding.addresEditText.setText(book?.address)
            binding.ownerEditText.setText(book?.owner)
        }

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()

        val name = binding.nameEditText.text
        val address = binding.addresEditText.text
        val owner = binding.ownerEditText.text
        binding.saveButton.setOnClickListener {
                if (name.isEmpty()) {
                    Toast.makeText(context, "Nama Toko Buku Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                } else if (address.isEmpty()){
                    Toast.makeText(context, "Alamat Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                } else if (owner.isEmpty()){
                    Toast.makeText(context, "Nama Pemilik Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else {
                if (book == null){
                    val book = Book(0, name.toString(), address.toString(), owner.toString(), currentLatLang?.latitude, currentLatLang?.longitude)
                    bookViewModel.insert(book)
                } else {
                    val book = Book(book?.id!!, name.toString(), address.toString(), owner.toString(), currentLatLang?.latitude, currentLatLang?.longitude)
                    bookViewModel.update(book)
                }
                findNavController().popBackStack()
            }

        }

        binding.button.setOnClickListener {
            book?.let {bookViewModel.delete(it) }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerDragListener(this)

    }

    override fun onMarkerDrag(p0: Marker) {}

    override fun onMarkerDragEnd(marker: Marker) {
        val newPosition = marker.position
        currentLatLang = LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context, currentLatLang.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragStart(p0: Marker) {

    }

    private fun checkPermission(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            Toast.makeText(applicationContext, "Akses lokasi ditolak", Toast.LENGTH_SHORT).show()

        }
    }
    private fun getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    var latLang = LatLng(location.latitude, location.longitude)
                    currentLatLang = latLang
                    var tittle = "Marker"

                    if (book != null) {
                        tittle = book?.name.toString()
                        val newCurrentLocation = LatLng(book?.latitude!!, book?.longitude!!)
                        latLang = newCurrentLocation
                    }
                    val markerOption = MarkerOptions()
                        .position(latLang)
                        .title(tittle)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bookshop))
                    mMap.addMarker(markerOption)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 15f))
                }
            }
    }
}