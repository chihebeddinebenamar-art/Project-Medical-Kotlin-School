package com.example.rendez_vous

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rendez_vous.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    companion object {
        private const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            // Initialiser Firebase Auth et Database
            auth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance()
            
            
        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization error", e)
            Toast.makeText(this, "Erreur d'initialisation Firebase: ${e.message}", Toast.LENGTH_LONG).show()
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val phone = binding.phoneInput.text.toString()
            val address = binding.addressInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            if (validateInput(name, email, phone, address, password, confirmPassword)) {
                registerUser(name, email, phone, address, password)
            }
        }

        binding.loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(name: String, email: String, phone: String, address: String, password: String) {
    // Désactiver le bouton pendant l'inscription
    binding.registerButton.isEnabled = false

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            binding.registerButton.isEnabled = true

            if (task.isSuccessful) {
                // Inscription réussie, sauvegarder les informations supplémentaires
                val user = auth.currentUser
                val userData = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "phone" to phone,
                    "address" to address
                )

                user?.let {
                    database.reference.child("users").child(it.uid)
                        .setValue(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Inscription réussie!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                }
            } else {
                Toast.makeText(this, "Erreur d'inscription", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        phone: String,
        address: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.nameInput.error = "Le nom est requis"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.emailInput.error = "L'email est requis"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.error = "Format d'email invalide"
            isValid = false
        }

        if (phone.isEmpty()) {
            binding.phoneInput.error = "Le numéro de téléphone est requis"
            isValid = false
        }

        if (address.isEmpty()) {
            binding.addressInput.error = "L'adresse est requise"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordInput.error = "Le mot de passe est requis"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordInput.error = "Le mot de passe doit contenir au moins 6 caractères"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordInput.error = "Veuillez confirmer votre mot de passe"
            isValid = false
        } else if (password != confirmPassword) {
            binding.confirmPasswordInput.error = "Les mots de passe ne correspondent pas"
            isValid = false
        }

        return isValid
    }
} 