package com.example.rendez_vous

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rendez_vous.databinding.ActivityDoctorDetailsBinding

class DoctorDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorDetailsBinding
    private lateinit var doctor: DoctorModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Récupérer les données du docteur depuis l'intent
        doctor = intent.getSerializableExtra("doctor") as DoctorModel

        // Configurer les vues avec les données du docteur
        setupViews()
        
        // Gestion du bouton retour
        binding.backButton.setOnClickListener {
            finish()
        }

        // Gestion du bouton d'appel
        binding.callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${doctor.phoneNumber}")
            }
            startActivity(intent)
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
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViews() {
        binding.doctorImage.setImageResource(doctor.imageRes)
        binding.doctorName.text = doctor.name
        binding.doctorSpecialty.text = doctor.specialty
        binding.doctorDescription.text = doctor.description
        
        // Afficher les horaires de travail
        displayWorkingHours()
    }

    private fun displayWorkingHours() {
        // Vider le conteneur d'horaires
        binding.hoursContainer.removeAllViews()

        // Créer une vue pour chaque jour
        doctor.workingHours.forEach { (day, hours) ->
            val dayView = TextView(this).apply {
                text = "$day: ${hours.joinToString(", ")}"
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.black, theme))
                setPadding(0, 8, 0, 8)
            }
            binding.hoursContainer.addView(dayView)
        }
    }
} 