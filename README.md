

# 🍽️ Restaurant Ordering App

## 📱 Overview

A native Android application that simulates a restaurant ordering experience.
Users can browse cuisines, select dishes, manage a cart, and place orders.
The app consumes REST APIs for real-time data and follows modern UI practices — **without any third-party libraries**.

---

## 🧩 Features

### 🏠 Home Screen

* **Cuisine Carousel:** Horizontal scroll of cuisine cards (e.g., North Indian, Chinese, etc.)
* **Top Dishes:** Highlights 3 popular dishes with image, price, rating, and add-to-cart option
* **Cart Button:** Redirects to the Cart screen
* **Language Switch:** Toggle between Hindi and English

### 🍛 Cuisine Menu Screen

* Displays dishes for the selected cuisine
* Each item shows image, price, and supports **+ / −** quantity controls

### 🛒 Cart Screen

* Lists selected cuisines and dishes
* Displays:

  * **Subtotal**
  * **Taxes** (CGST + SGST @ 2.5% each)
  * **Grand Total**
* **Place Order** button to complete the transaction

---

## 🔗 API Endpoints Used

| Endpoint              | Function                                    |
| --------------------- | ------------------------------------------- |
| `/get_item_list`      | Fetch all cuisines with their dishes        |
| `/get_item_by_id`     | Fetch details of a specific dish            |
| `/get_item_by_filter` | Filter dishes by cuisine, price, and rating |
| `/make_payment`       | Simulate order placement and payment        |

---

## 🚀 Tech Stack

* **Language:** Kotlin
* **UI:** Native Android Views
* **Architecture:** MVVM (optional)
* **Tools:** Android Studio, Emulator or Physical Device

---

## 📋 Notes

* Focused on native UI/UX — **No third-party libraries used**
* Fully functional with dynamic data using provided APIs
* Covers edge cases like empty cart, quantity limits, etc.

