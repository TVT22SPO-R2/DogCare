package com.example.dogcare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import PetsAdapter

interface PetDeleteListener {
    fun onDeletePet(petName: String)
}

class SettingsFragment : Fragment(), PetDeleteListener {

    private lateinit var adapter: PetsAdapter

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
            },
        )
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onDeletePet(petName: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Pet")
            .setMessage("Are you sure you want to delete $petName?")
            .setPositiveButton("Delete") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
