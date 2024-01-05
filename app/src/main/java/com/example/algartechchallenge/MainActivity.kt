package com.example.algartechchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.example.algartechchallenge.databinding.ActivityMainBinding
import com.example.algartechchallenge.viewmodel.MainViewState
import com.example.algartechchallenge.viewmodel.WeatherGeoViewModel
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
    private lateinit var adapter: ArrayAdapter<String>
    private var mapFragment: SupportMapFragment? = null
    private lateinit var coordinates: LatLng
    private var map: GoogleMap? = null
    private var marker: Marker? = null
    private var checkProximity = false
    private lateinit var binding: ActivityMainBinding

    /*private val startAutocomplete = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result: ActivityResult ->
            binding.autocompleteAddress1.setOnClickListener(startAutocompleteIntentListener)
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    Log.d(TAG, "Place: " + place.addressComponents)
                    fillInAddress(place)
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                Log.i(TAG, "User canceled autocomplete")
            }
        } as ActivityResultCallback<ActivityResult>)*/
    /*private fun startAutocompleteIntent() {
        val fields = listOf(
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG, Place.Field.VIEWPORT
        )

        // Build the autocomplete intent with field, country, and type filters applied
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setCountries(listOf("MEX"))
            .setTypesFilter(listOf(PlaceTypes.ADDRESS))
            .build(this)
        startAutocomplete.launch(intent)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = BuildConfig.PLACES_API_KEY
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "error_api_key", Toast.LENGTH_LONG).show()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        mapFragment =
            supportFragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG) as SupportMapFragment?

        // We only create a fragment if it doesn't already exist.
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

        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, emptyList())
        binding.autoCompleteTextView.setAdapter(adapter)

        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String

        }

        binding.searchButton.setOnClickListener {
            if (binding.autoCompleteTextView.text.toString().length > 6) {
                viewModel.getLocations(binding.autoCompleteTextView.text.toString(), apiKey)
            }
        }

        lifecycle.addObserver(viewModel)
        observe(viewModel.getViewState(), ::onViewState)

        setContentView(view)

    }

    private fun updateAdapter(results: List<String>) {
        adapter.clear()
        adapter.addAll(results)
        adapter.notifyDataSetChanged()
    }

    private fun onViewState(state: MainViewState?) {
        when (state) {
            MainViewState.Loading -> {

            }

            is MainViewState.ItemWeatherSearch -> {

            }

            is MainViewState.ItemsLocationSearch -> {
                updateAdapter(state.location.map { it.name })
            }

            is MainViewState.ErrorLoadingItem -> {

            }

            else -> {}
        }
    }


    /*private fun fillInAddress(place: Place) {
        val components = place.addressComponents
        val address1 = StringBuilder()
        val postcode = StringBuilder()

        if (components != null) {
            for (component in components.asList()) {
                when (component.types[0]) {
                    "street_number" -> {
                        address1.insert(0, component.name)
                    }
                    "route" -> {
                        address1.append(" ")
                        address1.append(component.shortName)
                    }
                    "postal_code" -> {
                        postcode.insert(0, component.name)
                    }
                    "postal_code_suffix" -> {
                        postcode.append("-").append(component.name)
                    }
                }
            }
        }
        binding.autocompleteAddress1.setText(address1.toString())
        showMap(place)
    }*/

    /*private fun showMap() {
        coordinates = place.latLng as LatLng
        mapFragment =
            supportFragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG) as SupportMapFragment?

        // We only create a fragment if it doesn't already exist.
        if (mapFragment == null) {
            mapPanel = (findViewById<View>(R.id.stub_map) as ViewStub).inflate()
            val mapOptions = GoogleMapOptions()
            mapOptions.mapToolbarEnabled(false)
            mapFragment = SupportMapFragment.newInstance(mapOptions)
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.confirmation_map,
                    mapFragment!!,
                    MAP_FRAGMENT_TAG
                )
                .commit()
            mapFragment!!.getMapAsync(this)
        } else {
            updateMap(coordinates)
        }
    }*/

    private fun updateMap(latLng: LatLng) {
        marker!!.position = latLng
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    // [START maps_solutions_android_autocomplete_map_ready]
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(19.8039297, -99.0930528), 15f))
        marker = map!!.addMarker(MarkerOptions().position(LatLng(19.8039297, -99.0930528)))
    }

    companion object {
        private const val MAP_FRAGMENT_TAG = "MAP"
    }
}