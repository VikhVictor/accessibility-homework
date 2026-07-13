package com.sweethome.shop.category

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sweethome.R
import com.sweethome.shop.catalog.CategoryViewModel
import com.sweethome.utils.StartEndOffsetItemDecoration

class CategoryRecyclerView(context: Context, attributeSet: AttributeSet): RecyclerView(context, attributeSet) {

    private val categoryAdapter: CategoryAdapter = CategoryAdapter()

    init {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        adapter = categoryAdapter
        addItemDecoration(StartEndOffsetItemDecoration(context.resources.getDimensionPixelOffset(R.dimen.catalog_offset)))
    }

    fun update(
        categoryViewModel: CategoryViewModel,
        itemClickListener: OnItemClickListener
    ) {
        categoryAdapter.setItemClickListener(itemClickListener)
        categoryAdapter.setItems(categoryViewModel.items)
    }

}