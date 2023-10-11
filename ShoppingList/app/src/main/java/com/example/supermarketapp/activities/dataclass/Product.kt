package com.example.supermarketapp.activities.dataclass

data class Product(val idProduct: String ,val name: String, val category: String, val quantity: Float,
val units: Int, val price: Float, var isSelected: Boolean = false)
