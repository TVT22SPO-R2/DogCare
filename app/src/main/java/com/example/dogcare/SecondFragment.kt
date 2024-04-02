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
import ActionAdapter
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import androidx.appcompat.app.AlertDialog


class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val db = FirebaseFirestore.getInstance()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var actionAdapter: ActionAdapter
    private var selectedItems: List<String> = emptyList()
    companion object{
        private const val TAG = "SecondFragment"
    }


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

        /*binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
        binding.fabDelete.setOnClickListener {
            if (!petName.isNullOrEmpty()) {
                deletePet(petName)
            }
        }

        binding.fetchTimeStamps.setOnClickListener {
            fetchTimestamps()
        }


    }



    private fun fetchPet(petName: String) {
        FirebaseUtils.fetchSinglePet(db, petName) { petData ->
            if (petData != null) {

                Log.d("FetchPet1", "Pet data1: $petData")

                val petName = petData["petName"]
                val timestamp = petData["dateTime"]
                val selectedActions = petData["selectedItems"] as? List<String> ?: emptyList()

                Log.d("FetchPet2", "Pet Name2: $petName, Timestamp2: $timestamp")
                Log.d("FetchActions", "Pet Actions: $selectedActions")

                binding.showPetName.text = "$petName\n$timestamp"
                selectedItems = selectedActions



                    val adapter = ActionAdapter(selectedActions ?: listOf()) { action ->
                        val currentTime = System.currentTimeMillis()
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        val formattedTime = dateFormat.format(currentTime)

                        storeTimeStampToDatabase(petName, action, formattedTime)

                        val message = "You have done $action at $formattedTime: "
                        showToast(message)
                    }
                    binding.idActions.adapter = adapter


                } else {

                    Log.d("FetchPet3", "Pet not found or error occurred.")
                }
            }

    }



    private fun storeTimeStampToDatabase(petName: Any?, action: String, timestamp: String){
        val data = hashMapOf(
            "petName" to petName,
            "action" to action,
            "timestamp" to timestamp
        )
        db.collection("timestamps")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Timestamp added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding timestamp", e)
            }
    }


    private fun fetchTimestamps() {
        val timestampsMap = mutableMapOf<String, String>()

        selectedItems.forEach { selectedItem ->
            db.collection("timestamps")
                .whereEqualTo("action", selectedItem)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val timestamp = documents.documents[0]["timestamp"] as String
                        timestampsMap[selectedItem] = timestamp
                    } else {
                        timestampsMap[selectedItem] = "No timestamp found"
                    }

                    if (timestampsMap.size == selectedItems.size) {
                        showTimestampsDialog(timestampsMap)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error getting timestamps for $selectedItem", e)
                }
        }
    }



    private fun showTimestampsDialog(timestampsMap: Map<String, String>) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("This pet's tasks were last done at")

        val message = StringBuilder()
        timestampsMap.forEach { (item, timestamp) ->
            message.append("$item: $timestamp\n")
        }
        alertDialogBuilder.setMessage(message.toString())

        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
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


