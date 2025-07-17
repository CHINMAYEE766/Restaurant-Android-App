package com.example.onebancrestaurant.ui.utils

import com.example.onebancrestaurant.ui.models.Cuisine
import com.example.onebancrestaurant.ui.models.Dish

object LocalData {

    fun getFallbackCuisines(): List<Cuisine> {
        return listOf(
            Cuisine(
                cuisine_id = "1",
                cuisine_name = "North Indian",
                cuisine_image_url = "local_north_indian", // fallback key
                items = listOf(
                    Dish("n1", "Butter Chicken", "local_butter_chicken", "199", "4.5"),
                    Dish("n2", "Paneer Tikka", "local_paneer_tikka", "179", "4.4")
                )
            ),
            Cuisine(
                cuisine_id = "2",
                cuisine_name = "Chinese",
                cuisine_image_url = "local_chinese",
                items = listOf(
                    Dish("c1", "Sweet and Sour Chicken", "local_sweet_sour", "189", "4.3"),
                    Dish("c2", "Hakka Noodles", "local_hakka_noodles", "159", "4.1")
                )
            ),
            Cuisine(
                cuisine_id = "3",
                cuisine_name = "Mexican",
                cuisine_image_url = "local_mexican",
                items = listOf(
                    Dish("m1", "Tacos", "local_tacos", "150", "4.5"),
                    Dish("m2", "Quesadilla", "local_quesadilla", "170", "4.2")
                )
            ),
            Cuisine(
                cuisine_id = "4",
                cuisine_name = "South Indian",
                cuisine_image_url = "local_south_indian",
                items = listOf(
                    Dish("s1", "Dosa", "local_dosa", "120", "4.6"),
                    Dish("s2", "Idli", "local_idli", "100", "4.3")
                )
            ),
            Cuisine(
                cuisine_id = "5",
                cuisine_name = "Italian",
                cuisine_image_url = "local_italian",
                items = listOf(
                    Dish("i1", "Pasta", "local_pasta", "180", "4.4"),
                    Dish("i2", "Pizza", "local_pizza", "200", "4.7")
                )
            )
        )
    }
}