package com.example.onebancrestaurant.ui

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.onebancrestaurant.R
import com.example.onebancrestaurant.ui.models.Dish
import com.example.onebancrestaurant.ui.utils.CartManager
import com.example.onebancrestaurant.ui.utils.LocalData
import com.example.onebancrestaurant.ui.utils.dishTranslations

class CuisineActivity : AppCompatActivity() {

    private lateinit var cuisineTitle: TextView
    private lateinit var dishContainer: LinearLayout
    private var isHindi = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuisine)

        cuisineTitle = findViewById(R.id.cuisineTitle)
        dishContainer = findViewById(R.id.dishContainer)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        isHindi = prefs.getBoolean("isHindi", false)

        val cuisineName = intent.getStringExtra("cuisine_name") ?: return
        cuisineTitle.text = if (isHindi) dishTranslations[cuisineName] ?: cuisineName else cuisineName

        val cuisine = LocalData.getFallbackCuisines().find { it.cuisine_name == cuisineName }
        cuisine?.items?.forEach { dish -> addDishView(dish) }
    }

    private fun addDishView(dish: Dish) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            val params = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 32)
            layoutParams = params
            setBackgroundResource(R.drawable.rounded_background)
        }

        val imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(500, 300)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        val resId = resources.getIdentifier(dish.image_url, "drawable", packageName)
        if (resId != 0) {
            imageView.setImageResource(resId)
        }

        val name = TextView(this).apply {
            text = if (isHindi) dishTranslations[dish.name] ?: dish.name else dish.name
            textSize = 16f
            gravity = Gravity.CENTER
        }

        val price = TextView(this).apply {
            text = "₹${dish.price} | ⭐ ${dish.rating}"
            textSize = 14f
            gravity = Gravity.CENTER
        }

        val quantityText = TextView(this).apply {
            text = CartManager.getDishQuantity(dish).toString()
            textSize = 16f
            setPadding(16, 0, 16, 0)
            gravity = Gravity.CENTER
        }

        val minusButton = Button(this).apply {
            text = "–"
            setOnClickListener {
                CartManager.removeDish(dish)
                quantityText.text = CartManager.getDishQuantity(dish).toString()
            }
        }

        val plusButton = Button(this).apply {
            text = "+"
            setOnClickListener {
                CartManager.addDish(dish)
                quantityText.text = CartManager.getDishQuantity(dish).toString()
            }
        }

        val quantityRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            addView(minusButton)
            addView(quantityText)
            addView(plusButton)
        }

        layout.addView(imageView)
        layout.addView(name)
        layout.addView(price)
        layout.addView(quantityRow)

        dishContainer.addView(layout)
    }
}
