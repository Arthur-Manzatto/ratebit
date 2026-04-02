package com.example.ratebit.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ratebit.R
import com.example.ratebit.model.User
import com.example.ratebit.repository.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignupActivity : AppCompatActivity() {

    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inputEmail = findViewById<TextInputEditText>(R.id.input_email)
        val inputPassword = findViewById<TextInputEditText>(R.id.input_password)
        val inputConfirmPassword = findViewById<TextInputEditText>(R.id.input_confirm_password)
        val btnSignUp = findViewById<MaterialButton>(R.id.btn_signup)
        val txtSignIn = findViewById<TextView>(R.id.txt_signin)
        val txtErrorMessage = findViewById<TextView>(R.id.txt_error_message)

        fun attemptSignUp() {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val confirmPassword = inputConfirmPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                txtErrorMessage.text = getString(R.string.invalid_email)
                txtErrorMessage.visibility = View.VISIBLE
                return
            }

            if (password != confirmPassword) {
                txtErrorMessage.text = getString(R.string.passwords_dont_match)
                txtErrorMessage.visibility = View.VISIBLE
                return
            }

            txtErrorMessage.visibility = View.GONE

            userRepository.getUser(email,
                onSuccess = { existingUser ->
                    if (existingUser != null) {
                        txtErrorMessage.text = getString(R.string.email_already_in_use)
                        txtErrorMessage.visibility = View.VISIBLE
                    } else {
                        val newUser = User(
                            email = email,
                            password = password,
                            name = email.substringBefore("@"),
                            type = "user"
                        )

                        userRepository.addUser(newUser,
                            onSuccess = {
                                val sharedPref = getSharedPreferences("ratebit_prefs", Context.MODE_PRIVATE)
                                sharedPref.edit {
                                    putString("user_email", email)
                                }

                                Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, GamesListActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            },
                            onFailure = {
                                Toast.makeText(this, "Error creating account", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                onFailure = {
                    Toast.makeText(this, "Error checking email", Toast.LENGTH_SHORT).show()
                }
            )
        }

        btnSignUp.setOnClickListener { attemptSignUp() }

        inputConfirmPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptSignUp()
                true
            } else false
        }

        txtSignIn.setOnClickListener {
            finish()
        }
    }
}