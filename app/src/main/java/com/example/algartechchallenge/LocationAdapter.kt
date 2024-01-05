package com.example.algartechchallenge

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.models.location.LocationModel

class LocationAdapter(context: Context, addresses: MutableList<LocationModel>) :
    ArrayAdapter<LocationModel>(context, R.layout.custom_spinner_layout, addresses) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_spinner_layout, parent, false)

        val address = getItem(position)

        val textViewName = view.findViewById<TextView>(R.id.textViewName)
        val textViewLatLng = view.findViewById<TextView>(R.id.textViewLatLng)

        textViewName.text = address?.name ?: ""
        textViewLatLng.text = "Latitud: ${address?.latitud}, Longitud: ${address?.longitud}"

        return view
    }
}