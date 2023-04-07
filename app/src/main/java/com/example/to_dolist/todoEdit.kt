package com.example.to_dolist

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class todoEdit: Fragment() {
    private var todo: MyTodo? = null
    private var listener: OnEditCompleteListener? = null

    private var pickDateBtn = view?.findViewById<Button>(R.id.idBtnPickDate)

    interface OnEditCompleteListener {
        fun onEditComplete(todo: MyTodo)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditCompleteListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnEditCompleteListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.todo_edit_page, container, false)

        // Get the todo item from the arguments
        todo = arguments?.getSerializable(ARG_TODO) as MyTodo?

        // Set the initial values of the EditTexts
        view.findViewById<EditText>(R.id.edtTitle).setText(todo?.title)
        view.findViewById<EditText>(R.id.edtDesc).setText(todo?.desc)
        view.findViewById<Button>(R.id.idBtnPickDate).text = todo?.date
        view.findViewById<Spinner>(R.id.spinnerCategory).setSelection(
            resources.getStringArray(R.array.categoryList).indexOf(todo?.category)
        )

        // Set up the "Update" button
        view.findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            // Update the todo item with the new values
            todo?.title = view.findViewById<EditText>(R.id.edtTitle).text.toString()
            todo?.desc = view.findViewById<EditText>(R.id.edtDesc).text.toString()
            todo?.date = view.findViewById<Button>(R.id.idBtnPickDate).text.toString()
            todo?.category = view.findViewById<Spinner>(R.id.spinnerCategory).selectedItem.toString()

            // Update the todo item in the database
            GlobalScope.launch {
                todoDatabase.getTodoDatabase(requireContext())?.todoDao()?.updateTodo(todo!!)
            }

            // Notify the listener that editing is complete
            listener?.onEditComplete(todo!!)

            // Navigate back to home page
            (activity as MainActivity).loadPage()
        }

        // Set up the "Pick Date" button
        view.findViewById<Button>(R.id.idBtnPickDate).setOnClickListener {
            // Show the date picker dialog
            showDatePickerDialog()
        }

        return view
    }

    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth ->
                pickDateBtn?.text =
                    (dayOfMonth.toString() + "\t-\t" + (monthOfYear + 1) + "\t-\t" + year)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    companion object {
        private const val ARG_TODO = "todo"

        @JvmStatic
        fun newInstance(todo: MyTodo) =
            todoEdit().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TODO, todo)
                }
            }
    }
}
