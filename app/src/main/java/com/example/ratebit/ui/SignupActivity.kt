package com.example.ratebit.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ratebit.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inputPassword = findViewById<TextInputEditText>(R.id.input_password)
        val inputConfirmPassword = findViewById<TextInputEditText>(R.id.input_confirm_password)
        val btnSignUp = findViewById<MaterialButton>(R.id.btn_signup)
        val txtSignIn = findViewById<TextView>(R.id.txt_signin)
        val txtErrorMessage = findViewById<TextView>(R.id.txt_error_message)

        btnSignUp.setOnClickListener {
            val password = inputPassword.text.toString()
            val confirmPassword = inputConfirmPassword.text.toString()

            if (password == confirmPassword) {
                txtErrorMessage.visibility = View.GONE
                // Lógica de sucesso aqui
            } else {
                txtErrorMessage.visibility = View.VISIBLE
            }
        }

        txtSignIn.setOnClickListener {
            finish()
        }
    }
}