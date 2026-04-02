package com.example.ratebit.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ratebit.R
import com.example.ratebit.repository.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val email = findViewById<TextInputEditText>(R.id.input_email)
        val password = findViewById<TextInputEditText>(R.id.input_password)
        val btnLogin = findViewById<MaterialButton>(R.id.btn_login)
        val btnSignUp = findViewById<TextView>(R.id.btn_signup)
        val txtErrorMessage = findViewById<TextView>(R.id.txt_error_message)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // FUNÇÃO DE LOGIN REUTILIZÁVEL
        fun attemptLogin() {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return
            }

            userRepository.getUser(emailText,
                onSuccess = { user ->
                    if (user != null && user.password == passwordText) {
                        txtErrorMessage.visibility = View.GONE
                        loginSuccess(user.email)
                    } else {
                        txtErrorMessage.visibility = View.VISIBLE
                    }
                },
                onFailure = {
                    Toast.makeText(this, "Login failed. Check your connection.", Toast.LENGTH_SHORT).show()
                }
            )
        }

        btnLogin.setOnClickListener { attemptLogin() }

        // DETECTAR ENTER NO TECLADO DA SENHA
        password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin()
                true
            } else false
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginSuccess(email: String) {
        val sharedPref = getSharedPreferences("ratebit_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("user_email", email).apply()

        val intent = Intent(this, GamesListActivity::class.java)
        startActivity(intent)
        finish()
    }
}