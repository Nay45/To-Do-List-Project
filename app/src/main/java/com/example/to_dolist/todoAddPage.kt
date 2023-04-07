package com.example.to_dolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class todoAddPage: Fragment() {
    private var db: todoDatabase?=null
    private var todoDao: todoDao? = null

    private var titleInput: String = ""
    private var descInput: String = ""
    private var dateInput: String = ""
    private var categoryInput: String = ""

    private var edtTitle: EditText? = null
    private var edtDesc: EditText? = null
    private var edtDate: Button? = null
    private var btnSave: Button? = null
    private var spinnerCategory: Spinner? = null

    lateinit var pickDateBtn: Button

    companion object{
        fun newInstance():todoAddPage{
            return todoAddPage()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todo_add_page,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLocalDB()
        initView()

        pickDateBtn = view.findViewById(R.id.idBtnPickDate)

        pickDateBtn.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { view, year, monthOfYear, dayOfMonth ->
                    pickDateBtn.text =
                        (dayOfMonth.toString() + "\t-\t" + (monthOfYear + 1) + "\t-\t" + year)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    private fun initLocalDB() {
        db = todoDatabase.getTodoDatabase(requireActivity())
        todoDao = db?.todoDao()
        setDataCatagery()
    }

    private fun setDataCatagery() {
        val adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.categoryList, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory?.adapter = adapter
    }

    private fun initView() {
        edtTitle = activity?.findViewById(R.id.edtTitle)
        edtDesc = activity?.findViewById(R.id.edtDesc)
        edtDate = activity?.findViewById(R.id.idBtnPickDate)
        spinnerCategory = activity?.findViewById(R.id.spinnerCategory)

        btnSave = activity?.findViewById(R.id.btnSave)
        btnSave?.setOnClickListener {
            validasiInput()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        db?.close()
        edtTitle = null
        edtDesc = null
        edtDate = null
        btnSave = null
        spinnerCategory = null
    }

    private fun validasiInput() {
        titleInput = edtTitle?.text.toString()
        descInput = edtDesc?.text.toString()
        dateInput = edtDate?.text.toString()
        categoryInput = spinnerCategory?.selectedItem.toString()

        if (titleInput.isEmpty() || descInput.isEmpty() || dateInput.isEmpty() || categoryInput.equals("Choose Category")) {
            Toast.makeText(requireActivity(), "U Miss Something Check Again", Toast.LENGTH_SHORT).show()
        } else {
            val data = MyTodo(title = titleInput, desc = descInput, date = dateInput, category = categoryInput)
            addTodo(data)
            Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addTodo(data: MyTodo): Job {
        return GlobalScope.launch {
            todoDao?.addTodo(data)
            (activity as MainActivity).loadAdd()
        }
    }
}