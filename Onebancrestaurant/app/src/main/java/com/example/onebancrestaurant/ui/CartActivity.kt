package com.example.onebancrestaurant.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.onebancrestaurant.R
import com.example.onebancrestaurant.ui.models.Dish
import com.example.onebancrestaurant.ui.utils.ApiClient
import com.example.onebancrestaurant.ui.utils.CartManager
import com.example.onebancrestaurant.ui.utils.dishTranslations
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    private lateinit var cartItemContainer: LinearLayout
    private lateinit var netTotalText: TextView
    private lateinit var taxText: TextView
    private lateinit var grandTotalText: TextView
    private lateinit var placeOrderButton: Button
    private var isHindi = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartItemContainer = findViewById(R.id.cartItemContainer)
        netTotalText = findViewById(R.id.netTotalText)
        taxText = findViewById(R.id.taxText)
        grandTotalText = findViewById(R.id.grandTotalText)
        placeOrderButton = findViewById(R.id.placeOrderButton)


        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        isHindi = prefs.getBoolean("isHindi", false)

        displayCartItems()

        placeOrderButton.setOnClickListener {
            makePayment()
        }
    }

    private fun displayCartItems() {
        cartItemContainer.removeAllViews()
        val cartItems = CartManager.getCartItems()

        if (cartItems.isEmpty()) {
            cartItemContainer.addView(TextView(this).apply {
                text = if (isHindi) "आपकी गाड़ी खाली है।" else "Your cart is empty."
                textSize = 16f
            })
            return
        }

        for (dish in cartItems) {
            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(16, 16, 16, 16)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                gravity = Gravity.CENTER_VERTICAL
            }

            val nameText = TextView(this).apply {
                val name = if (isHindi) dishTranslations[dish.name] ?: dish.name else dish.name
                text = "$name - ₹${dish.price}"
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val minusButton = Button(this).apply {
                text = "-"
                setOnClickListener {
                    CartManager.removeDish(dish)
                    displayCartItems()
                }
            }

            val quantityText = TextView(this).apply {
                text = dish.quantity.toString()
                textSize = 16f
                setPadding(16, 0, 16, 0)
            }

            val plusButton = Button(this).apply {
                text = "+"
                setOnClickListener {
                    CartManager.addDish(dish)
                    displayCartItems()
                }
            }

            itemLayout.addView(nameText)
            itemLayout.addView(minusButton)
            itemLayout.addView(quantityText)
            itemLayout.addView(plusButton)

            cartItemContainer.addView(itemLayout)
        }

        val netTotal = CartManager.getTotalAmount()
        val tax = (netTotal * 0.05).toInt()
        val grandTotal = netTotal + tax

        netTotalText.text = if (isHindi) "कुल: ₹$netTotal" else "Net Total: ₹$netTotal"
        taxText.text = if (isHindi) "कर (CGST + SGST 5%): ₹$tax" else "CGST + SGST (5%): ₹$tax"
        grandTotalText.text = if (isHindi) "कुल योग: ₹$grandTotal" else "Grand Total: ₹$grandTotal"
    }

    private fun makePayment() {
        val cartItems = CartManager.getCartItems()
        val jsonData = JSONArray()
        var totalItems = 0

        for (dish in cartItems) {
            totalItems += dish.quantity
            val item = JSONObject()
            item.put("cuisine_id", "0")  // optional or update if needed
            item.put("item_id", dish.id)
            item.put("item_price", dish.price.toIntOrNull() ?: 0)
            item.put("item_quantity", dish.quantity)
            jsonData.put(item)
        }

        val totalAmount = CartManager.getTotalAmount() + (CartManager.getTotalAmount() * 0.05).toInt()

        val requestBody = JSONObject()
        requestBody.put("total_amount", totalAmount)
        requestBody.put("total_items", totalItems)
        requestBody.put("data", jsonData)

        ApiClient.makePayment(requestBody.toString()) { response ->
            runOnUiThread {
                if (response != null) {
                    Toast.makeText(
                        this,
                        if (isHindi) "ऑर्डर सफलतापूर्वक किया गया ✅" else "Order Placed Successfully ✅",
                        Toast.LENGTH_LONG
                    ).show()
                    CartManager.clearCart()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        if (isHindi) "ऑर्डर विफल रहा ❌" else "Order failed ❌",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
