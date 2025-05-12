package com.example.billingbudpoe

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Income : AppCompatActivity() {
    private lateinit var incomeExpenseText: TextView
    private lateinit var minGoalEditText: EditText
    private lateinit var maxGoalEditText: EditText
    private lateinit var saveGoalsButton: Button

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        incomeExpenseText = findViewById(R.id.incomeExpenseText)
        minGoalEditText = findViewById(R.id.minGoalEditText)
        maxGoalEditText = findViewById(R.id.maxGoalEditText)
        saveGoalsButton = findViewById(R.id.saveGoalsButton)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val income = document.getDouble("income") ?: 0.0
                        val expenses = document.getDouble("expenses") ?: 0.0
                        val minGoal = document.getDouble("minGoal") ?: 0.0
                        val maxGoal = document.getDouble("maxGoal") ?: 0.0
                        incomeExpenseText.text = "Income: R ${"%,.2f".format(income)}\nExpenses: R ${"%,.2f".format(expenses)}"

                        minGoalEditText.setText(minGoal.toString())
                        maxGoalEditText.setText(maxGoal.toString())
                    }
                }
                .addOnFailureListener {
                    incomeExpenseText.text = "Failed to load income and expenses."
                }

            saveGoalsButton.setOnClickListener {
                val minGoal = minGoalEditText.text.toString().toDoubleOrNull()
                val maxGoal = maxGoalEditText.text.toString().toDoubleOrNull()

                if (minGoal != null && maxGoal != null) {
                    val updates = hashMapOf(
                        "minGoal" to minGoal,
                        "maxGoal" to maxGoal
                    )
                    db.collection("users").document(userId)
                        .update(updates as Map<String, Any>)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Goals saved!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to save goals.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Please enter valid numbers for both goals.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            incomeExpenseText.text = "User not logged in."
        }
    }
}