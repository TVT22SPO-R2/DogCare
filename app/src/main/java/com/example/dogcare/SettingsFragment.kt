package com.example.dogcare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import PetsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingsFragment : Fragment() {

    private lateinit var adapter: PetsAdapter
    private lateinit var tvInformation: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewPets)

        adapter = PetsAdapter(
            onItemClick = { petName ->
                // Handle item click here
            }
        )
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize TextView
        tvInformation = view.findViewById(R.id.tvInformation)
        tvInformation.text = "This app helps pet owners to keep track of their pets\n\n\n\nCreators:\n\n\nToni Isopoussu\n\nJyri Karhu\n\nHenri Mustakangas\n\nEelis Keränen\n\nNiko Matilainen"
        tvInformation.setTextColor(resources.getColor(R.color.orange2))
        tvInformation.visibility = View.GONE // Hide initially

        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            // Show the text when the FAB is pressed
            tvInformation.visibility = View.VISIBLE
        }

        return view
    }
}
