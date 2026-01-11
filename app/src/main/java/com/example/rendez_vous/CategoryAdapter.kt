package com.example.rendez_vous

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CategoryAdapter(
    private val items: List<CategoryModel>,
    private val onClick: (CategoryModel) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val button: MaterialButton) : RecyclerView.ViewHolder(button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val button = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false) as MaterialButton
        return CategoryViewHolder(button)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]
        holder.button.text = item.name
        holder.button.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size
}