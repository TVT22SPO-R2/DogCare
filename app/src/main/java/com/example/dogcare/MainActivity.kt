package com.example.dogcare

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.dogcare.databinding.ActivityMainBinding
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            // Create a layout inflater to inflate the custom layout
            val inflater = LayoutInflater.from(view.context)
            val dialogView = inflater.inflate(R.layout.custom_dialog_layout, null)

            // Initialize TextInputEditText
            val editText = dialogView.findViewById<TextInputEditText>(R.id.textInput)
            editText.hint = "Enter pet name"

            // List of items to display
            val items = arrayOf("Walked", "Fed", "Washed", "Nails trimmed", "Is the pet alone")
            val checkedItems = booleanArrayOf(false, false, false, false, false) // Initially non of the items are checked


            // Create an AlertDialog
            val alertDialogBuilder = AlertDialog.Builder(view.context)
            alertDialogBuilder.setView(dialogView)
            alertDialogBuilder.setTitle("Add Pet") // Set your dialog title here

            alertDialogBuilder.setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            // Set positive button click listener
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                val userInput = editText.text.toString() // Get the text entered by the use

                //Get selected items
                val selectedItems = mutableListOf<String>()
                for (i in items.indices) {
                    if (checkedItems[i]) {
                        selectedItems.add(items[i])
                    }
                }


                // Add the data to Firebase Firestore
                val db = FirebaseFirestore.getInstance()
                val data = hashMapOf(
                    "petName" to userInput,
                    "selectedItems" to selectedItems,
                    "dateTime" to System.currentTimeMillis()
                )
                db.collection("pets")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        // Handle success if needed
                        dialog.dismiss()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                        // Handle failure if needed
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

