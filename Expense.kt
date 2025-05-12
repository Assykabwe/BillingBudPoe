package com.example.billingbudpoe

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class Expense : AppCompatActivity() {
    private val calendar = Calendar.getInstance()
    private val expenseList = mutableListOf<String>() // Simulated data
    private var photoBitmap: Bitmap? = null
    private lateinit var menuIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        // Initialize views
        val backIcon: ImageView = findViewById(R.id.imageView3)
        menuIcon = findViewById(R.id.imageView4) // Make sure this matches the menu icon ID
        // Handle back button click
        backIcon.setOnClickListener {
            finish()
        }
        // Menu icon click opens popup
        menuIcon.setOnClickListener {
            val popupMenu = PopupMenu(this, menuIcon)
            popupMenu.menuInflater.inflate(R.menu.category_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_income -> startActivity(Intent(this, Income::class.java))
                    R.id.menu_expense -> startActivity(Intent(this, Expense::class.java))
                    R.id.menu_reports -> startActivity(Intent(this, Reports::class.java))
                    R.id.menu_settings -> startActivity(Intent(this, Settings::class.java))
                }
                true
            }

            popupMenu.show()
        }

        val addCategoryButton: Button = findViewById(R.id.addCategoryButton)
        val addExpenseButton: Button = findViewById(R.id.addExpenseButton)
        val photoButton: Button = findViewById(R.id.photoButton)
        val expenseListView: ListView = findViewById(R.id.expenseListView)
        val fromDateBtn: Button = findViewById(R.id.fromDateButton)
        val toDateBtn: Button = findViewById(R.id.toDateButton)

        val categories = mutableListOf("Food", "Travel", "Shopping")
        val categorySpinner: Spinner = findViewById(R.id.categorySpinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        addCategoryButton.setOnClickListener {
            val input = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Add Category")
                .setView(input)
                .setPositiveButton("Add") { _, _ ->
                    val newCategory = input.text.toString()
                    if (newCategory.isNotEmpty()) {
                        categories.add(newCategory)
                        spinnerAdapter.notifyDataSetChanged()
                    }
                }.setNegativeButton("Cancel", null)
                .show()
        }
        addExpenseButton.setOnClickListener {
            val selectedCategory = categorySpinner.selectedItem.toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val currentDateTime = sdf.format(Date())
            val entry = "[$currentDateTime] Category: $selectedCategory - Description: Sample Entry"
            expenseList.add(entry)
            if (photoBitmap != null) {
                expenseList.add("Photo attached 📷")
            }

            val listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, expenseList)
            expenseListView.adapter = listAdapter
        }

        photoButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1001)
        }

        fromDateBtn.setOnClickListener {
            showDatePickerDialog("From")
        }

        toDateBtn.setOnClickListener {
            showDatePickerDialog("To")
        }
    }

    private fun showDatePickerDialog(label: String) {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dateStr = "$label Date: $year-${month + 1}-$day"
            Toast.makeText(this, dateStr, Toast.LENGTH_SHORT).show()
        }

        DatePickerDialog(
            this,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            photoBitmap = data?.extras?.get("data") as Bitmap
            Toast.makeText(this, "Photo captured successfully", Toast.LENGTH_SHORT).show()
        }
    }
}