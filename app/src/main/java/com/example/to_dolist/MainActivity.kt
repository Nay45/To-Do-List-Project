package com.example.to_dolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(todoPage())

        bottomNav = findViewById(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navList -> {
                    loadFragment(todoPage())
                    true
                }
                R.id.navAdd -> {
                    loadFragment(todoAddPage())
                    true
                }
                else -> {false}
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame,fragment)
        transaction.commit()
    }

    fun loadPage() {
        loadFragment(todoPage())
    }

    fun loadAdd() {
        loadFragment(todoAddPage())
    }
}