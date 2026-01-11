package com.example.rendez_vous

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rendez_vous.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val doctors = mutableListOf<DoctorModel>()
    private lateinit var doctorAdapter: DoctorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialiser Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Vérifier si l'utilisateur est connecté
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }


        // Categories RecyclerView (statique)
        val categories = listOf(
            CategoryModel(1, "Denteeth"),
            CategoryModel(2, "Theripist"),
            CategoryModel(3, "Surgery"),
            CategoryModel(4, "Cardiology"),
            CategoryModel(5, "Neurology")
        )
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.adapter = CategoryAdapter(categories) {
            // Action on category click (optionnel)
        }

        // Liste statique de docteurs (mock data)
        val doctors = listOf(
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
        // Commenter le code dynamique Firebase
        // database = FirebaseDatabase.getInstance().reference
        // doctorAdapter = DoctorAdapter(doctors) { doctor ->
        //     val intent = Intent(this, DoctorDetailsActivity::class.java).apply {
        //         putExtra("doctor", doctor)
        //     }
        //     startActivity(intent)
        // }
        // binding.doctorsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        // binding.doctorsRecyclerView.adapter = doctorAdapter
        // loadDoctorsFromFirebase()
        // Utiliser la liste statique
        doctorAdapter = DoctorAdapter(doctors) { doctor ->
            val intent = Intent(this, DoctorDetailsActivity::class.java).apply {
                putExtra("doctor", doctor)
            }
            startActivity(intent)
        }
        binding.doctorsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.doctorsRecyclerView.adapter = doctorAdapter

        // Navigation vers AllDoctorsActivity
        binding.doctorsSeeAll.setOnClickListener {
            startActivity(Intent(this, AllDoctorsActivity::class.java))
        }

        // Gestion du menu de navigation en bas
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_doctors -> {
                    startActivity(Intent(this, AllDoctorsActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadDoctorsFromFirebase() {
        database.child("doctors").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                doctors.clear()
                for (doctorSnap in snapshot.children) {
                    val doctor = doctorSnap.getValue(DoctorModel::class.java)
                    if (doctor != null) {
                        doctors.add(doctor)
                    }
                }
                doctorAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Erreur de chargement: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


}