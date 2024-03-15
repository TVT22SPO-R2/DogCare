package com.example.dogcare

import PetsAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dogcare.databinding.FragmentFirstBinding
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogcare.R
import com.example.dogcare.FirstFragmentDirections

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private lateinit var petsAdapter: PetsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize RecycleView and adapter
        petsAdapter = PetsAdapter { petName ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment()
            findNavController().navigate(action)

        }

        //Set layout manager and adapter for RecycleView
        binding.recycleViewPets.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewPets.adapter = petsAdapter

        //Fetch pets
        fetchPets()

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchPets() {
        db.collection("pets")
            .get()
            .addOnSuccessListener { documents ->
                val petsList = mutableListOf<String>()
                for (document in documents) {
                    val petName = document.getString("petName")
                    petName?.let {
                        petsList.add(it)
                    }
                }
                petsAdapter.submitList(petsList)
            }
            .addOnFailureListener { exception ->
                // Handle errors here
            }
    }
}