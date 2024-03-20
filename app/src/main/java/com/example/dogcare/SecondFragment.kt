package com.example.dogcare

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.dogcare.databinding.FragmentSecondBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.example.dogcare.ActionAdapter

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val db = FirebaseFirestore.getInstance()
    private lateinit var appBarConfiguration: AppBarConfiguration


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve pet name passed as an argument
        val petName = arguments?.let {
            SecondFragmentArgs.fromBundle(it).petName
        }

        if (!petName.isNullOrEmpty()) {
            fetchPet(petName)
        }
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.fabDelete.setOnClickListener {
            if (!petName.isNullOrEmpty()) {
                deletePet(petName)
            }
        }

    }

    private fun fetchPet(petName: String) {
        FirebaseUtils.fetchSinglePet(db, petName) { petData ->
            if (petData != null) {

                Log.d("FetchPet1", "Pet data1: $petData")

                val petName = petData["petName"]
                val timestamp = petData["dateTime"]
                val selectedActions = petData["selectedItems"] as? List<String>

                Log.d("FetchPet2", "Pet Name2: $petName, Timestamp2: $timestamp")
                Log.d("FetchActions","Pet Actions: $selectedActions")

                binding.showPetName.text = "$petName\n$timestamp"


                val adapter = ActionAdapter(selectedActions ?: listOf())
                Log.d("FetchActions","Pet Actions: $adapter")// Ensure adapter is always initialized
                binding.idActions.adapter = adapter

            } else {

                Log.d("FetchPet3", "Pet not found or error occurred.")
            }
        }
    }
    private fun deletePet(petName: String) {
        FirebaseUtils.deletePet(db, petName)
            .addOnSuccessListener {

                Toast.makeText(requireContext(), "Pet deleted successfully", Toast.LENGTH_SHORT).show()

                findNavController().navigateUp()
            }
            .addOnFailureListener { exception ->

                Log.e("DeletePet", "Error deleting pet: ${exception.message}", exception)
                Toast.makeText(requireContext(), "Failed to delete pet", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}