import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogcare.R


class ActionAdapter(private val actions: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<ActionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionTextView: TextView = itemView.findViewById(R.id.actionTextView)

        init {
            actionTextView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(actions[position])
                }
            }
        }

        fun bind(action: String) {
            actionTextView.text = action
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(actions[position])
    }

    override fun getItemCount(): Int {
        return actions.size
    }
}