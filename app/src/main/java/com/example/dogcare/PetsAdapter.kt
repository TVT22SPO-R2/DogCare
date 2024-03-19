import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogcare.FirebaseUtils
import com.example.dogcare.R
import com.google.firebase.firestore.FirebaseFirestore

class PetsAdapter(private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<PetsAdapter.PetsViewHolder>() {

    private var petsList: List<String> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetsViewHolder, position: Int) {
        val petName = petsList[position]
        holder.bind(petName)
    }

    override fun getItemCount(): Int = petsList.size

    fun submitList(petsList: List<String>) {
        this.petsList = petsList
        notifyDataSetChanged()
    }

    inner class PetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petNameTextView: TextView = itemView.findViewById(R.id.textPetName)

        init {
            itemView.setOnClickListener {
                onItemClick(petsList[adapterPosition])
            }
        }

        fun bind(petName: String) {
            petNameTextView.text = petName
        }
    }

}
