package com.sweethome.shop.catalog

import com.sweethome.item.FullItemViewModel

data class CategoryViewModel(
    val title: String,
    val items: ArrayList<FullItemViewModel>
)
