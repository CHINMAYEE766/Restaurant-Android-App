package com.example.onebancrestaurant.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onebancrestaurant.R
import com.example.onebancrestaurant.ui.models.Cuisine
import com.example.onebancrestaurant.ui.utils.cuisineTranslations

class CuisineAdapter(
    private val cuisines: List<Cuisine>,
    private val isHindi: Boolean,
    private val onCuisineClick: (Cuisine) -> Unit
) : RecyclerView.Adapter<CuisineAdapter.CuisineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cuisine_card, parent, false)
        return CuisineViewHolder(view)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: CuisineViewHolder, position: Int) {
        val actualPosition = position % cuisines.size
        holder.bind(cuisines[actualPosition])
    }

    inner class CuisineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cuisineImage: ImageView = itemView.findViewById(R.id.cuisineImage)
        private val cuisineName: TextView = itemView.findViewById(R.id.cuisineName)

        fun bind(cuisine: Cuisine) {
            cuisineName.text = if (isHindi)
                cuisineTranslations[cuisine.cuisine_name] ?: cuisine.cuisine_name
            else
                cuisine.cuisine_name

            val resId = itemView.context.resources.getIdentifier(
                cuisine.cuisine_image_url, "drawable", itemView.context.packageName
            )

            if (resId != 0) {
                cuisineImage.setImageResource(resId)
            }

            itemView.setOnClickListener {
                onCuisineClick(cuisine)
            }
        }
    }
}
