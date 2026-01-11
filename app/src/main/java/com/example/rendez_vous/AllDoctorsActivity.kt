package com.example.rendez_vous

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rendez_vous.databinding.ActivityAllDoctorsBinding
import android.text.Editable
import android.text.TextWatcher

class AllDoctorsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllDoctorsBinding
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var allDoctors: List<DoctorModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gestion du bouton de retour
        binding.backButton.setOnClickListener {
            finish()
        }

        // Liste statique de docteurs (mock data)
        allDoctors = listOf(
            DoctorModel(
                id = 1,
                name = "Dr. Ahmed Ben Salah",
                specialty = "Cardiologue",
                rating = 4.9,
                imageRes = R.drawable.ic_profile,
                description = "Spécialiste en cardiologie à l'hôpital La Rabta, Tunis, avec plus de 15 ans d'expérience.",
                workingHours = mapOf(
                    "Lun" to listOf("09:00", "11:00"),
                    "Mer" to listOf("10:00", "12:00")
                ),
                phoneNumber = "+21620123456"
            ),
            DoctorModel(
                id = 2,
                name = "Dr. Leila Trabelsi",
                specialty = "Dentiste",
                rating = 4.7,
                imageRes = R.drawable.ic_profile,
                description = "Dentiste à Sousse, spécialiste en orthodontie et soins esthétiques.",
                workingHours = mapOf(
                    "Mar" to listOf("08:30", "12:00"),
                    "Jeu" to listOf("14:00", "17:00")
                ),
                phoneNumber = "+21622112233"
            ),
            DoctorModel(
                id = 3,
                name = "Dr. Sirine",
                specialty = "Dentiste",
                rating = 4.7,
                imageRes = R.drawable.ic_profile,
                description = "Dentiste à Sousse, spécialiste en orthodontie et soins esthétiques.",
                workingHours = mapOf(
                    "Mar" to listOf("08:30", "12:00"),
                    "Jeu" to listOf("14:00", "17:00")
                ),
                phoneNumber = "+21622112233"
            ),
            DoctorModel(
                id = 3,
                name = "Dr. Hichem Bouzid",
                specialty = "Chirurgien",
                rating = 4.8,
                imageRes = R.drawable.ic_profile,
                description = "Chirurgien général à l'hôpital Charles Nicolle, Tunis.",
                workingHours = mapOf(
                    "Lun" to listOf("10:00", "13:00"),
                    "Ven" to listOf("09:00", "12:00")
                ),
                phoneNumber = "+21623456789"
            ),
            DoctorModel(
                id = 4,
                name = "Dr. Amira Gharbi",
                specialty = "Pédiatre",
                rating = 4.6,
                imageRes = R.drawable.ic_profile,
                description = "Pédiatre à Monastir, attentive et à l'écoute des enfants.",
                workingHours = mapOf(
                    "Mer" to listOf("09:00", "12:00"),
                    "Sam" to listOf("10:00", "13:00")
                ),
                phoneNumber = "+21624567890"
            ),
            DoctorModel(
                id = 5,
                name = "Dr. Sami Jaziri",
                specialty = "Neurologue",
                rating = 4.5,
                imageRes = R.drawable.ic_profile,
                description = "Neurologue à Sfax, spécialiste des troubles du sommeil.",
                workingHours = mapOf(
                    "Mar" to listOf("09:00", "11:00"),
                    "Jeu" to listOf("15:00", "18:00")
                ),
                phoneNumber = "+21625678901"
            )
        )

        doctorAdapter = DoctorAdapter(allDoctors.toMutableList()) { doctor ->
            // Lancer DoctorDetailsActivity avec les données du docteur
            val intent = Intent(this, DoctorDetailsActivity::class.java).apply {
                putExtra("doctor", doctor)
            }
            startActivity(intent)
        }
        
        // Utilisation d'un LinearLayoutManager vertical
        binding.allDoctorsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.allDoctorsRecyclerView.adapter = doctorAdapter

        // Filtrage en temps réel
        binding.searchEditTextAll.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDoctors(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Gestion du menu de navigation
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_doctors -> true  // Déjà dans l'activité des docteurs
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun filterDoctors(query: String) {
        val filtered = allDoctors.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.specialty.contains(query, ignoreCase = true)
        }
        doctorAdapter = DoctorAdapter(filtered) { doctor ->
            // Lancer DoctorDetailsActivity avec les données du docteur
            val intent = Intent(this, DoctorDetailsActivity::class.java).apply {
                putExtra("doctor", doctor)
            }
            startActivity(intent)
        }
        binding.allDoctorsRecyclerView.adapter = doctorAdapter
    }
} 