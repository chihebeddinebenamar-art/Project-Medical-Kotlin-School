package com.example.rendez_vous

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class DoctorAdapter(
    private val items: List<DoctorModel>,  // Liste des médecins à afficher
    private val onBookClick: (DoctorModel) -> Unit  // Fonction de callback pour le clic sur "Book"
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    // Classe interne pour gérer les vues d'un élément
    inner class DoctorViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
    ) {
        // Références aux vues dans item_doctor.xml
        val doctorImage: ImageView = itemView.findViewById(R.id.doctorImage)
        val doctorName: TextView = itemView.findViewById(R.id.doctorName)
        val doctorSpecialty: TextView = itemView.findViewById(R.id.doctorSpecialty)
        val doctorRating: TextView = itemView.findViewById(R.id.doctorRating)
        val bookButton: MaterialButton = itemView.findViewById(R.id.bookButton)
    }

    // Crée une nouvelle vue pour chaque élément
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        return DoctorViewHolder(parent)
    }

    // Remplit les données dans les vues
    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val item = items[position]
        holder.doctorImage.setImageResource(item.imageRes)
        holder.doctorName.text = item.name
        holder.doctorSpecialty.text = item.specialty
        holder.doctorRating.text = item.rating.toString()
        holder.bookButton.setOnClickListener { onBookClick(item) }
    }

    // Retourne le nombre total d'éléments
    override fun getItemCount(): Int = items.size
}