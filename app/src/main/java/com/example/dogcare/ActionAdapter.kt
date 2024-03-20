package com.example.dogcare

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActionAdapter(private val actions: List<String>) : RecyclerView.Adapter<ActionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // No need to define TextViews here since they will be added dynamically
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false)
        Log.d("Actions","Actions: $view")
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the parent layout (LinearLayout)
        val parentLayout = holder.itemView as LinearLayout

        // Loop through the actions list and create TextViews dynamically
        actions.forEach { action ->
            val textView = TextView(parentLayout.context)
            Log.d("Fs","Pet Actions: $textView")
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            textView.text = action
            Log.d("ActionAdapter","Adapters: $action")
            parentLayout.addView(textView)
        }
    }

    override fun getItemCount(): Int {
        return 1 // Since we're creating TextViews dynamically, we only need one item
    }
}