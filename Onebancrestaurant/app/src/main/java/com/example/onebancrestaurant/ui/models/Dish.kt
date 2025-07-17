package com.example.onebancrestaurant.ui.models

data class Dish(
    val id: String,
    val name: String,
    val image_url: String,
    val price: String,
    val rating: String,
    var quantity: Int = 0
)