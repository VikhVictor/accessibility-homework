package com.sweethome.item

import kotlin.collections.ArrayList

data class FullItemViewModel(
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
    val colors: ArrayList<Color>,
    val discount: Boolean = false
)

data class Color(val colorValue: Int, val colorName: String)
