package com.example.dogcare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dogcare.databinding.FragmentSecondBinding
<<<<<<< Updated upstream
=======
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import java.util.Date
import com.example.dogcare.ActionAdapter


>>>>>>> Stashed changes

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

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

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
<<<<<<< Updated upstream
=======
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
>>>>>>> Stashed changes
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}