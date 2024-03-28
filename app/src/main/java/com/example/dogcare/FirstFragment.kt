package com.example.dogcare

import PetsAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dogcare.databinding.FragmentFirstBinding
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogcare.R
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dogcare.FirstFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.*
import java.util.*
import java.text.ParseException
import java.util.*
import kotlin.random.Random

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

        binding.checkPetsButton.setOnClickListener {
            checkPetsStatus()
        }

        // Example button setup to fetch and show activity
        binding.fetchActivity.setOnClickListener {
            fetchAndShowActivity()
        }

        // Initialize RecycleView and adapter
        petsAdapter = PetsAdapter { petName ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(petName)
            findNavController().navigate(action)
        }

        //Set layout manager and adapter for RecycleView
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycleViewPets.layoutManager = layoutManager
        binding.recycleViewPets.adapter = petsAdapter

        //Fetch pets
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

            // Create the AlertDialog
            val alertDialog = alertDialogBuilder.create()

            // Set text color for the buttons
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.orange2))
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.orange2))
            }

            // Show the AlertDialog
            alertDialog.show()

        }
    }



    fun checkPetsStatus() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        Log.d(TAG, "Checking pets status...")
        db.collection("pets")
            .get()
            .addOnSuccessListener { documents ->
                Log.d(TAG, "Successfully retrieved ${documents.size()} pets.")
                for (document in documents) {
                    val dateTimeString = document.getString("dateTime") // Haetaan dateTime String-muodossa
                    try {
                        val lastActionDate = dateTimeString?.let { dateFormat.parse(it) } // Muunnetaan String Date-objektiksi
                        lastActionDate?.let {
                            // Tarkistetaan, onko viimeisestä toimenpiteestä kulunut yli 3 tuntia
                            if (System.currentTimeMillis() - it.time > 3 * 60 * 60 * 1000) {
                                // Yli 3 tuntia on kulunut, näytä pop-up
                                val petName = document.getString("petName") ?: getString(R.string.your_pet)
                                showAlert(petName, getString(R.string.needs_attention))
                            }
                        }
                    } catch (e: ParseException) {
                        Log.e(TAG, "Error parsing the date", e)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun showAlert(petName: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(petName)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
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

    // Could not find a API that does this, so had to make a list
    private val petActivities = listOf(
        "Go for a walk in the park.",
        "Teach your pet a new trick.",
        "Have a mini photoshoot with your pet.",
        "Play hide and seek.",
        "Set up a pet playdate with a friend's pet.",
        "Try a DIY pet toy or treat recipe.",
        "Have a relaxing pet spa day at home.",
        "Visit a pet-friendly cafe or store.",
        "Explore a new trail or beach together.",
        "Have a picnic with your pet's favorite snacks.",
        "Create a treasure hunt for your pet with their favorite treats.",
        "Build an obstacle course in your backyard or living room for your pet.",
        "Give your pet a gentle massage or grooming session."
    )

    private fun fetchAndShowActivity() {
        // Randomly select an activity
        val activity = petActivities[Random.nextInt(petActivities.size)]

        // Using MaterialAlertDialogBuilder for the pop-up
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.pet_activity_title))
            .setMessage(activity)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }


    fun petFetch() {
        FirebaseUtils.fetchPets(db, petsAdapter)
    }
}
