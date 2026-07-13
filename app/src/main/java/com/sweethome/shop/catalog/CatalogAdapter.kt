package com.sweethome.shop.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sweethome.R
import com.sweethome.shop.category.CategoryRecyclerView
import com.sweethome.shop.category.OnItemClickListener

class CatalogAdapter : RecyclerView.Adapter<CategoryViewHolder>() {

    private val catalogItems: ArrayList<CategoryViewModel> = arrayListOf()
    private lateinit var itemClickListener: OnItemClickListener

    fun setCatalogItems(items: ArrayList<CategoryViewModel>) {
        catalogItems.clear()
        catalogItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(R.layout.catalog_category_item, parent, false)
            )
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        return catalogItems.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(catalogItems[position], itemClickListener)
    }
}


class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val category: CategoryRecyclerView = itemView.findViewById(R.id.category)
    private val title: TextView = itemView.findViewById(R.id.title)

    fun bind(categoryViewModel: CategoryViewModel, itemClickListener: OnItemClickListener) {
        category.update(categoryViewModel, itemClickListener)
        title.text = categoryViewModel.title
    }
}