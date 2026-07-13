package com.sweethome.domain.model

data class Product(
    val id: String,
    val category: String,
    val collection: String,
    val image: String,
    val model: String,
    val price: String,
    val currency: String,
    val about: String,
    val country: String,
    val manufacturer: String,
    val designer: String,
    val color: Int,
    val colorName: String,
    val colors: List<ProductColor>,
    val discount: Boolean = false
)

data class ProductColor(
    val colorValue: Int,
    val colorName: String
)
