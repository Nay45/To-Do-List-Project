package com.example.to_dolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class todoPage: Fragment() {
    private var listTodoRecyclerView: RecyclerView? = null

    private var listTodo: List<MyTodo>? = null

    private var db: todoDatabase? = null
    private var todoDao: todoDao? = null

    companion object{
        fun newInstance():todoPage{
            return todoPage()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todo_page,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLocalDB()
        initView()
    }

    private fun initLocalDB() {
        db = todoDatabase.getTodoDatabase(requireActivity())
        todoDao = db?.todoDao()
    }

    private fun initView() {
        listTodoRecyclerView = activity?.findViewById(R.id.listTodo)
        ambilDataTeman()
    }

    private fun ambilDataTeman() {
        listTodo = ArrayList()
        todoDao?.selectAllTodo()?.observe(requireActivity()) { r -> listTodo = r
            when {
                listTodo?.size == 0 -> showToast("Not Yet")
                else -> {
                    showingTodo()
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        listTodoRecyclerView = null
    }

    private fun showingTodo() {
        listTodoRecyclerView?.layoutManager = LinearLayoutManager(activity)
        listTodoRecyclerView?.adapter = todoAdapter(requireActivity(),
            listTodo!! as ArrayList<MyTodo>
        )
    }
}