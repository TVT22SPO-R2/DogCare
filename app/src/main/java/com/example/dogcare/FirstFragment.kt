package com.example.dogcare

import PetsAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.example.dogcare.databinding.FragmentFirstBinding
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogcare.R
import com.example.dogcare.FirstFragmentDirections
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Locale


import android.os.Handler
import android.os.Looper
import android.widget.Toast

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    val currentTime = System.currentTimeMillis()

    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private lateinit var petsAdapter: PetsAdapter
    private val TAG = "FirstFragment"
    private val checkBoxes = mutableListOf<CheckBox>()


    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedDateTime = dateFormat.format(currentTime)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecycleView and adapter
        petsAdapter = PetsAdapter { petName ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(petName)
            findNavController().navigate(action)
        }

        //Set layout manager and adapter for RecycleView
        binding.recycleViewPets.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewPets.adapter = petsAdapter

        //Fetch pets
        //fetchPets()
        petFetch()

        binding.fab.setOnClickListener { view ->
            // Create a layout inflater to inflate the custom layout
            val inflater = LayoutInflater.from(view.context)
            val dialogView = inflater.inflate(R.layout.custom_dialog_layout, null)

            // Initialize TextInputEditText
            val editText = dialogView.findViewById<TextInputEditText>(R.id.textInput)
            editText.hint = "Enter pet name"

            // List of items to display
            val items = arrayOf("Walked", "Fed", "Washed", "Nails trimmed", "Is the pet alone")
            val checkedItems = booleanArrayOf(false, false, false, false, false) // Initially none of the items are checked

            // Find the container for checkboxes in the custom dialog layout
            val checkBoxContainer = dialogView.findViewById<LinearLayout>(R.id.checkboxContainer)

            // Clear any existing checkboxes
            checkBoxContainer.removeAllViews()

            // Create and add checkboxes dynamically
            for (item in items) {
                val checkBox = CheckBox(view.context)
                checkBox.text = item
                checkBoxContainer.addView(checkBox)
            }

            // Create an AlertDialog
            val alertDialogBuilder = AlertDialog.Builder(view.context)
            alertDialogBuilder.setView(dialogView)
            alertDialogBuilder.setTitle("Add Pet") // Set your dialog title here

            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                val userInput = editText.text.toString() // Get the text entered by the user

                // Get selected items
                val selectedItems = mutableListOf<String>()
                for ((index, item) in items.withIndex()) {
                    val checkBox = checkBoxContainer.getChildAt(index) as CheckBox
                    if (checkBox.isChecked) {
                        selectedItems.add(item)
                    }
                }

                // Add the data to Firebase Firestore
                val data = hashMapOf(
                    "petName" to userInput,
                    "selectedItems" to selectedItems,
                    "dateTime" to formattedDateTime
                )
                db.collection("pets")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        petFetch()
                        dialog.dismiss()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                // Check if "Walked" or "Fed" is one of the selected items and set a reminder
                selectedItems.forEach { action ->
                    when (action) {
                        "Walked" -> setReminder(userInput, "needs to be walked again!")
                        "Fed" -> setReminder(userInput, "needs to be fed again!")
                    }
                }
            }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                // Handle negative button click if needed
                dialog.dismiss()
            }

            // Create and show the dialog
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun setReminder(petName: String, message: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(requireContext(), "$petName $message", Toast.LENGTH_LONG).show()
        }, 2000) // 2 second delay for testing
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    fun petFetch() {
        FirebaseUtils.fetchPets(db, petsAdapter)
    }
}