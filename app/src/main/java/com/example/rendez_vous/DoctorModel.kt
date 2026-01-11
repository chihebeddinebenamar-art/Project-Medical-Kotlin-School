package com.example.rendez_vous

import java.io.Serializable

data class DoctorModel(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
    val imageRes: Int,
    val description: String,
    val workingHours: Map<String, List<String>>,
    val phoneNumber: String
) : Serializable 