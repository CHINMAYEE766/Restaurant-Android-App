package com.example.onebancrestaurant.ui

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.onebancrestaurant.R
import com.example.onebancrestaurant.ui.adapters.CuisineAdapter
import com.example.onebancrestaurant.ui.models.Dish
import com.example.onebancrestaurant.ui.utils.*

class MainActivity : AppCompatActivity() {

    private lateinit var cuisineRecyclerView: RecyclerView
    private lateinit var topDishesContainer: LinearLayout
    private lateinit var cuisineAdapter: CuisineAdapter
    private var isHindi = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cuisineRecyclerView = findViewById(R.id.cuisineRecyclerView)
        topDishesContainer = findViewById(R.id.topDishesContainer)

        val cartButton = findViewById<Button>(R.id.cartButton)
        val languageButton = findViewById<Button>(R.id.languageToggleButton)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        isHindi = prefs.getBoolean("isHindi", false)
        languageButton.text = if (isHindi) "Switch to English" else "Switch to Hindi"

        cuisineRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        LinearSnapHelper().attachToRecyclerView(cuisineRecyclerView)

        cartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        languageButton.setOnClickListener {
            isHindi = !isHindi
            prefs.edit().putBoolean("isHindi", isHindi).apply()
            recreate()
        }

        loadLocalDataOnly()
        loadTopDishes()
    }

    override fun onResume() {
        super.onResume()
        loadTopDishes() // Refresh quantity values
    }

    private fun loadLocalDataOnly() {
        val fallbackCuisines = LocalData.getFallbackCuisines()
        val infiniteList = List(1000) { i -> fallbackCuisines[i % fallbackCuisines.size] }

        cuisineAdapter = CuisineAdapter(infiniteList, isHindi) { selectedCuisine ->
            val intent = Intent(this, CuisineActivity::class.java)
            intent.putExtra("cuisine_name", selectedCuisine.cuisine_name)
            startActivity(intent)
        }

        cuisineRecyclerView.adapter = cuisineAdapter
        cuisineRecyclerView.scrollToPosition(infiniteList.size / 2)
    }

    private fun loadTopDishes() {
        topDishesContainer.removeAllViews()
        val fallbackCuisines = LocalData.getFallbackCuisines()
        val topThree = fallbackCuisines.flatMap { it.items }.take(3)
        topThree.forEach { dish -> addTopDishView(dish) }
    }

    private fun addTopDishView(dish: Dish) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(4, 4, 4, 4)
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 10)
            }
            setBackgroundResource(R.drawable.rounded_card_background)
        }

        val imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 100)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        val resId = resources.getIdentifier(dish.image_url, "drawable", packageName)
        if (resId != 0) {
            imageView.setImageResource(resId)
        }

        val name = TextView(this).apply {
            text = if (isHindi) dishTranslations[dish.name] ?: dish.name else dish.name
            textSize = 13f
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 2, 0, 2)
        }

        val price = TextView(this).apply {
            text = "₹${dish.price} | ⭐ ${dish.rating}"
            textSize = 11f
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val quantityText = TextView(this).apply {
            text = "Qty: ${CartManager.getDishQuantity(dish)}"
            textSize = 11f
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 3, 0, 3)
        }

        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER

            val minusButton = Button(this@MainActivity).apply {
                text = "-"
                textSize = 11f
                width = 42
                setTextColor(ContextCompat.getColor(context, R.color.buttonTextColor))
                background = ContextCompat.getDrawable(context, R.drawable.rounded_green_button)
                setOnClickListener {
                    CartManager.removeDish(dish)
                    quantityText.text = "Qty: ${CartManager.getDishQuantity(dish)}"
                }
            }

            val plusButton = Button(this@MainActivity).apply {
                text = "+"
                textSize = 11f
                width = 42
                setTextColor(ContextCompat.getColor(context, R.color.buttonTextColor))
                background = ContextCompat.getDrawable(context, R.drawable.rounded_green_button)
                setOnClickListener {
                    CartManager.addDish(dish)
                    quantityText.text = "Qty: ${CartManager.getDishQuantity(dish)}"
                }
            }

            addView(minusButton)
            addView(plusButton)
        }

        layout.addView(imageView)
        layout.addView(name)
        layout.addView(price)
        layout.addView(quantityText)
        layout.addView(buttonLayout)

        topDishesContainer.addView(layout)
    }
}
