package com.example.dogcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogcare.R

class PetsAdapter(
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<PetsAdapter.PetsViewHolder>() {

    var petsList: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetsViewHolder, position: Int) {
        val petName = petsList[position]
        holder.bind(petName)
        holder.itemView.setOnClickListener {
            onItemClick(petName)
        }
    }

    override fun getItemCount(): Int = petsList.size

    inner class PetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petNameTextView: TextView = itemView.findViewById(R.id.textPetName)

        fun bind(petName: String) {
            petNameTextView.text = petName
        }
    }
}
