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


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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
            val items = arrayOf("Item 1", "Item 2", "Item 3")

            // Create an AlertDialog
            val alertDialogBuilder = AlertDialog.Builder(view.context)
            alertDialogBuilder.setView(dialogView)
            alertDialogBuilder.setTitle("Add Pet") // Set your dialog title here
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                val userInput = editText.text.toString()
                // Handle positive button click if needed
                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                // Handle negative button click if needed
                dialog.dismiss()
            }

            // Set up the list of items
            alertDialogBuilder.setItems(items) { dialog, which ->
                // Handle item click
                val selectedItem = items[which]
                // Do something with the selected item
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}