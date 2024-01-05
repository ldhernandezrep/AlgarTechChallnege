package com.example.algartechchallenge

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var binding: ActivityMainBinding
    private var locationModel = LocationModel(19.8039297, -99.0930528, "Zumpango")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeViews()
        setupMapFragment()

        binding.apply {
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val locationModel = adapter.getItem(position)
                    if (locationModel != null) {
                        viewModel.getWeather(locationModel.latitud, locationModel.longitud, BuildConfig.APP_ID_WEATHER)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Manejar caso cuando no se ha seleccionado nada (opcional)
                }
            }

            searchButton.setOnClickListener {
                if (autoCompleteTextView.text.toString().length > 6) {
                    viewModel.getLocations(autoCompleteTextView.text.toString(), BuildConfig.PLACES_API_KEY)
                }
            }

            clearButton.setOnClickListener {
                autoCompleteTextView.visibility = View.VISIBLE
                spinner.visibility = View.GONE
            }

            lifecycle.addObserver(viewModel)
            observe(viewModel.getViewState(), ::onViewState)
        }
    }

    private fun initializeViews() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        adapter = LocationAdapter(this, mutableListOf())
    }

    private fun setupMapFragment() {
        val apiKey = BuildConfig.PLACES_API_KEY
        val appId = BuildConfig.APP_ID_WEATHER
        if (apiKey.isEmpty() || appId.isEmpty()) {
            Toast.makeText(this, "error_api_key", Toast.LENGTH_LONG).show()
            return
        }

        mapFragment = supportFragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG) as SupportMapFragment?

        if (mapFragment == null) {
            val mapOptions = GoogleMapOptions()
            mapOptions.mapToolbarEnabled(false)
            mapFragment = SupportMapFragment.newInstance(mapOptions)
            supportFragmentManager.beginTransaction().add(R.id.confirmation_map, mapFragment!!).commit()
            mapFragment!!.getMapAsync(this)
        }
    }

    private fun onViewState(state: MainViewState?) {
        binding.apply {
            when (state) {
                MainViewState.Loading -> {
                    lyMain.visibility = View.GONE
                    llProgressBar.root.visibility = View.VISIBLE
                }

                is MainViewState.ItemWeatherSearch -> {
                    lyMain.visibility = View.VISIBLE
                    llProgressBar.root.visibility = View.GONE
                    updateMap(
                        state.weather.latitud,
                        state.weather.longitud,
                        state.weather.main,
                        state.weather.temp,
                        state.weather.name
                    )
                }

                is MainViewState.ItemsLocationSearch -> {
                    autoCompleteTextView.visibility = View.GONE
                    spinner.visibility = View.VISIBLE
                    lyMain.visibility = View.VISIBLE
                    llProgressBar.root.visibility = View.GONE
                    updateAdapter(state.location)
                }

                is MainViewState.ErrorLoadingItem -> {
                    autoCompleteTextView.visibility = View.VISIBLE
                    spinner.visibility = View.GONE
                    lyMain.visibility = View.VISIBLE
                    llProgressBar.root.visibility = View.GONE
                }

                else -> {
                }
            }
        }
    }

    private fun updateAdapter(results: List<LocationModel>) {
        val data = results.toMutableList()
        adapter.clear()
        adapter.addAll(data)
        adapter.notifyDataSetChanged()
    }

    private fun updateMap(latitud: Double, longitud: Double, clima: String, temp: Double, name: String) {
        marker!!.position = LatLng(latitud, longitud)
        marker!!.title = name

        val snippetContent = "<html><body><b>Clima:</b> $clima<br/><b>Temperatura:</b> $temp</body></html>"
        marker!!.snippet = snippetContent

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
