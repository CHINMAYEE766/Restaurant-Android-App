package com.example.onebancrestaurant.ui.utils

import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object ApiClient {

    private const val BASE_URL = "https://uat.onebanc.ai/emulator/interview/"
    private const val API_KEY = "uonebancservceemultrS3cg8RaL30"


    fun getItemList(callback: (String?) -> Unit) {
        thread {
            try {
                val url = URL(BASE_URL + "get_item_list")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("X-Partner-API-Key", API_KEY)
                conn.setRequestProperty("X-Forward-Proxy-Action", "get_item_list")
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                val jsonBody = JSONObject().apply {
                    put("page", 1)
                    put("count", 10)
                }

                BufferedWriter(OutputStreamWriter(conn.outputStream, "UTF-8")).use { it.write(jsonBody.toString()) }

                val inputStream = if (conn.responseCode == HttpURLConnection.HTTP_OK) conn.inputStream else conn.errorStream
                val response = inputStream.bufferedReader().use { it.readText() }

                callback(response)
                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }


    fun getAllItems(callback: (String?) -> Unit) {
        thread {
            try {
                val allCuisines = JSONArray()
                var currentPage = 1
                val pageSize = 10
                var totalPages = 1

                while (currentPage <= totalPages) {
                    val url = URL(BASE_URL + "get_item_list")
                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.setRequestProperty("X-Partner-API-Key", API_KEY)
                    conn.setRequestProperty("X-Forward-Proxy-Action", "get_item_list")
                    conn.setRequestProperty("Content-Type", "application/json")
                    conn.doOutput = true

                    val jsonBody = JSONObject()
                    jsonBody.put("page", currentPage)
                    jsonBody.put("count", pageSize)

                    BufferedWriter(OutputStreamWriter(conn.outputStream, "UTF-8")).use { it.write(jsonBody.toString()) }

                    val inputStream = if (conn.responseCode == HttpURLConnection.HTTP_OK) conn.inputStream else conn.errorStream
                    val responseStr = inputStream.bufferedReader().use { it.readText() }
                    val responseJson = JSONObject(responseStr)

                    val cuisines = responseJson.getJSONArray("cuisines")
                    for (i in 0 until cuisines.length()) {
                        allCuisines.put(cuisines.getJSONObject(i))
                    }

                    totalPages = responseJson.optInt("total_pages", 1)
                    currentPage++
                    conn.disconnect()
                }

                val finalResult = JSONObject()
                finalResult.put("cuisines", allCuisines)
                callback(finalResult.toString())

            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }


    fun getItemsByFilter(cuisineType: String, callback: (String?) -> Unit) {
        thread {
            try {
                val url = URL(BASE_URL + "get_item_by_filter")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("X-Partner-API-Key", API_KEY)
                conn.setRequestProperty("X-Forward-Proxy-Action", "get_item_by_filter")
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                val jsonBody = JSONObject().apply {
                    put("cuisine_type", listOf(cuisineType))
                }

                BufferedWriter(OutputStreamWriter(conn.outputStream, "UTF-8")).use { it.write(jsonBody.toString()) }

                val inputStream = if (conn.responseCode == HttpURLConnection.HTTP_OK) conn.inputStream else conn.errorStream
                val response = inputStream.bufferedReader().use { it.readText() }

                callback(response)
                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }


    fun makePayment(jsonBody: String, callback: (String?) -> Unit) {
        thread {
            try {
                val url = URL(BASE_URL + "make_payment")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("X-Partner-API-Key", API_KEY)
                conn.setRequestProperty("X-Forward-Proxy-Action", "make_payment")
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                BufferedWriter(OutputStreamWriter(conn.outputStream, "UTF-8")).use { it.write(jsonBody) }

                val inputStream = if (conn.responseCode == HttpURLConnection.HTTP_OK) conn.inputStream else conn.errorStream
                val response = inputStream.bufferedReader().use { it.readText() }

                callback(response)
                conn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }
}
