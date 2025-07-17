package com.example.onebancrestaurant.ui.utils

import com.example.onebancrestaurant.ui.models.Dish

object CartManager {
    private val cart = mutableMapOf<String, Dish>()


    fun addDish(dish: Dish) {
        val existing = cart[dish.id]
        if (existing != null) {
            existing.quantity += 1
        } else {

            val newDish = dish.copy(quantity = 1)
            cart[dish.id] = newDish
        }
    }


    fun removeDish(dish: Dish) {
        val existing = cart[dish.id]
        if (existing != null) {
            if (existing.quantity > 1) {
                existing.quantity -= 1
            } else {
                cart.remove(dish.id)
            }
        }
    }
    fun getDishQuantity(dish: Dish): Int {
        return cart[dish.id]?.quantity ?: 0
    }



    fun getCartItems(): List<Dish> = cart.values.toList()


    fun getTotalAmount(): Int = cart.values.sumBy {
        (it.price.toIntOrNull() ?: 0) * it.quantity
    }


    fun clearCart() = cart.clear()
}
