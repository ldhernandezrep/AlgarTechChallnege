package com.example.algartechchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewStub
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.example.algartechchallenge.databinding.ActivityMainBinding
import com.example.algartechchallenge.viewmodel.MainViewState
import com.example.algartechchallenge.viewmodel.WeatherGeoViewModel
import com.example.models.location.LocationModel
import com.example.utilities.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: WeatherGeoViewModel by viewModels()
    private lateinit var adapter: LocationAdapter
    private var mapFragment: SupportMapFragment? = null
    private lateinit var coordinates: LatLng
    private var map: GoogleMap? = null
    private var marker: Marker? = null
    private var checkProximity = false
    private lateinit var binding: ActivityMainBinding
    private var locationModel = LocationModel(19.8039297, -99.0930528, "Zumpango")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = BuildConfig.PLACES_API_KEY
        val appId = BuildConfig.APP_ID_WEATHER
        if (apiKey.isEmpty() || appId.isEmpty()) {
            Toast.makeText(this, "error_api_key", Toast.LENGTH_LONG).show()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        mapFragment =
            supportFragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG) as SupportMapFragment?

        if (mapFragment == null) {
            val mapOptions = GoogleMapOptions()
            mapOptions.mapToolbarEnabled(false)
            mapFragment = SupportMapFragment.newInstance(mapOptions)
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.confirmation_map,
                    mapFragment!!
                )
                .commit()
            mapFragment!!.getMapAsync(this)
        }

        adapter = LocationAdapter(this, mutableListOf())
        binding.spinner.setAdapter(adapter)


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val locationModel = adapter.getItem(position)
                if (locationModel != null && locationModel.latitud!= 0.0 && locationModel.longitud !=0.0) {
                    viewModel.getWeather(locationModel.latitud, locationModel.longitud,appId, locationModel.name)
                }else{
                    Toast.makeText(applicationContext,"No hay seleccion",Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar caso cuando no se ha seleccionado nada (opcional)
                Toast.makeText(applicationContext,"No hay seleccion",Toast.LENGTH_LONG).show()
            }
        }

        binding.searchButton.setOnClickListener {
            if (binding.autoCompleteTextView.text.toString().length >= 5) {
                viewModel.getLocations(binding.autoCompleteTextView.text.toString(), apiKey)
            }
        }

        binding.clearButton.setOnClickListener{
            binding.autoCompleteTextView.visibility = View.VISIBLE
            binding.spinner.visibility = View.GONE
            binding.autoCompleteTextView.setText("")
            adapter.clear()
            adapter.notifyDataSetChanged()
        }

        lifecycle.addObserver(viewModel)
        observe(viewModel.getViewState(), ::onViewState)

        setContentView(view)

    }

    private fun updateAdapter(results: List<LocationModel>) {
        val defaultOption = LocationModel(0.0, 0.0, "Selecciona una opción")
        val data = mutableListOf(defaultOption)
        data.addAll(results)
        adapter.clear()
        adapter.addAll(data)
        adapter.notifyDataSetChanged()
        binding.spinner.setSelection(0)
    }

    private fun onViewState(state: MainViewState?) {
        when (state) {
            MainViewState.Loading -> {
                binding.lyMain.visibility = View.GONE
                binding.llProgressBar.root.visibility = View.VISIBLE
            }

            is MainViewState.ItemWeatherSearch -> {
                binding.lyMain.visibility = View.VISIBLE
                binding.llProgressBar.root.visibility = View.GONE
                updateMap(
                    state.weather.latitud,
                    state.weather.longitud,
                    state.weather.main,
                    state.weather.temp,
                    state.weather.name
                )
            }

            is MainViewState.ItemsLocationSearch -> {
                binding.autoCompleteTextView.visibility = View.GONE
                binding.spinner.visibility = View.VISIBLE
                binding.lyMain.visibility = View.VISIBLE
                binding.llProgressBar.root.visibility = View.GONE
                updateAdapter(state.location)
            }

            is MainViewState.ErrorLoadingItem -> {
                binding.autoCompleteTextView.visibility = View.VISIBLE
                binding.spinner.visibility = View.GONE
                binding.lyMain.visibility = View.VISIBLE
                binding.llProgressBar.root.visibility = View.GONE
                adapter.clear()
                adapter.notifyDataSetChanged()
            }

            else -> {}
        }
    }

    private fun updateMap(
        latitud: Double,
        longitud: Double,
        clima: String,
        temp: Double,
        name: String
    ) {
        marker!!.position = LatLng(latitud, longitud)
        marker!!.title = name
        marker!!.snippet = "Clima : ${clima} , Temperatura : ${temp}"
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitud, longitud), 15f))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    locationModel.latitud,
                    locationModel.longitud
                ), 15f
            )
        )
        marker = map!!.addMarker(
            MarkerOptions().position(
                LatLng(
                    locationModel.latitud,
                    locationModel.longitud
                )
            )
        )
    }

    companion object {
        private const val MAP_FRAGMENT_TAG = "MAP"
    }
}