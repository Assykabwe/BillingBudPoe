package com.example.billingbudpoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView

class Home : AppCompatActivity() {

    private lateinit var incomeButton: Button
    private lateinit var expenseButton: Button
    private lateinit var investmentButton: Button
    private lateinit var reportButton: Button
    private lateinit var settingsButton: Button
    private lateinit var logoutButton: Button
    private lateinit var menuIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize buttons
        incomeButton = findViewById(R.id.income)
        expenseButton = findViewById(R.id.expenses)
        investmentButton = findViewById(R.id.investment)
        reportButton = findViewById(R.id.report)
        settingsButton = findViewById(R.id.settings)
        logoutButton = findViewById(R.id.logout)
        menuIcon = findViewById(R.id.imageView4) // Menu icon

        // Capture the user name
        val user = FirebaseAuth.getInstance().currentUser
        val nameTextView: TextView = findViewById(R.id.fullName)
        nameTextView.text = user?.displayName ?: "User"

        // Button click listeners
        incomeButton.setOnClickListener {
            startActivity(Intent(this, Income::class.java))
        }

        expenseButton.setOnClickListener {
            startActivity(Intent(this, Expense::class.java))
        }

        investmentButton.setOnClickListener {
            startActivity(Intent(this, Investment::class.java))
        }

        reportButton.setOnClickListener {
            startActivity(Intent(this, Reports::class.java))
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, Login::class.java))
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
                    R.id.menu_investment -> startActivity(Intent(this, Investment::class.java))
                    R.id.menu_reports -> startActivity(Intent(this, Reports::class.java))
                    R.id.menu_settings -> startActivity(Intent(this, Settings::class.java))
                }
                true
            }

            popupMenu.show()
        }
    }
}