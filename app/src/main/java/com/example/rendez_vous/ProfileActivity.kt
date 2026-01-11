package com.example.rendez_vous

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rendez_vous.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Charger les infos du profil depuis Firebase
        database.child("users").child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.profileName.text = snapshot.child("name").getValue(String::class.java) ?: ""
                binding.profileEmail.text = snapshot.child("email").getValue(String::class.java) ?: ""
                binding.profilePhone.text = snapshot.child("phone").getValue(String::class.java) ?: ""
                binding.profileAddress.text = snapshot.child("address").getValue(String::class.java) ?: ""
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Erreur de chargement: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })

        // Déconnexion
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Modification du profil
        binding.editProfileButton.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null)
            val nameEdit = dialogView.findViewById<EditText>(R.id.editName)
            val phoneEdit = dialogView.findViewById<EditText>(R.id.editPhone)
            val addressEdit = dialogView.findViewById<EditText>(R.id.editAddress)

            // Pré-remplir les champs
            nameEdit.setText(binding.profileName.text)
            phoneEdit.setText(binding.profilePhone.text)
            addressEdit.setText(binding.profileAddress.text)

            AlertDialog.Builder(this)
                .setTitle("Modifier le profil")
                .setView(dialogView)
                .setPositiveButton("Enregistrer") { _, _ ->
                    val newName = nameEdit.text.toString()
                    val newPhone = phoneEdit.text.toString()
                    val newAddress = addressEdit.text.toString()
                    val user = auth.currentUser
                    if (user != null) {
                        val updates = mapOf(
                            "name" to newName,
                            "phone" to newPhone,
                            "address" to newAddress
                        )
                        database.child("users").child(user.uid).updateChildren(updates).addOnSuccessListener {
                            binding.profileName.text = newName
                            binding.profilePhone.text = newPhone
                            binding.profileAddress.text = newAddress
                            Toast.makeText(this, "Profil mis à jour", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Annuler", null)
                .show()
        }

        // Flèche de retour
        binding.backButton.setOnClickListener {
            finish()
        }

        // Gestion du menu de navigation
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_doctors -> {
                    startActivity(Intent(this, AllDoctorsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> true  // Déjà dans l'activité de profil
                else -> false
            }
        }
    }
} 