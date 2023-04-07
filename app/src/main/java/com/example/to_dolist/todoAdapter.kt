package com.example.to_dolist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class todoAdapter (private val context: Context, private val items: ArrayList<MyTodo>):
    RecyclerView.Adapter<todoAdapter.ViewHolder>(){

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        private var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        private var txtDesc: TextView = itemView.findViewById(R.id.txtDesc)
        private var txtDate: TextView = itemView.findViewById(R.id.txtDate)
        private var txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
        var btnUpdate: ImageView = itemView.findViewById(R.id.listUpdate)
        var btnDelete: ImageView = itemView.findViewById(R.id.listDel)

        fun bindItem(item: MyTodo) {
            txtTitle.text = item.title
            txtDesc.text = item.desc
            txtDate.text = item.date
            txtCategory.text = item.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.todo_result, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position))

        val item = items[position]
        holder.bindItem(item)

        holder.btnDelete.setOnClickListener {

            Toast.makeText(holder.itemView.context, "${item.title} deleted", Toast.LENGTH_SHORT).show()

            GlobalScope.launch {
                todoDatabase.getTodoDatabase(context)?.todoDao()?.deleteTodo(item)
            }

            items.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.btnUpdate.setOnClickListener {
            // Show a toast to indicate the edit button was clicked
            Toast.makeText(holder.itemView.context, "Edit clicked for ${item.title}", Toast.LENGTH_SHORT).show()

            // Open the edit activity and pass the item's ID to it
            val intent = Intent(holder.itemView.context, todoEdit::class.java)
            intent.putExtra("itemId", item.todoId)
            holder.itemView.context.startActivity(intent)

            // Notify the adapter that the item at this position has changed,
            // so that it will update the item's view in the RecyclerView
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int = items.size
}