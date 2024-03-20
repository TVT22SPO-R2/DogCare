import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogcare.R

class ActionAdapter(private val actions: List<String>) : RecyclerView.Adapter<ActionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionContainer: LinearLayout = itemView.findViewById(R.id.actionContainer)

        fun bind(actions: List<String>) {
            actionContainer.removeAllViews()
            for (action in actions) {
                val textView = TextView(itemView.context)
                textView.text = action
                actionContainer.addView(textView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(actions)
    }

    override fun getItemCount(): Int {
        return 1 // Since we're creating TextViews dynamically, we only need one item
    }
}