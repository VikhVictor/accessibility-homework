package com.sweethome.shop.category

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sweethome.R
import com.sweethome.item.FullItemViewModel

class CategoryAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    private lateinit var itemClickListener: OnItemClickListener
    private val items: ArrayList<FullItemViewModel> = arrayListOf()

    fun setItems(items: ArrayList<FullItemViewModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.category_item_layout, parent, false
        ))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}


class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.name)
    private val manufacturer: TextView = itemView.findViewById(R.id.manufacturer)
    private val price: TextView = itemView.findViewById(R.id.price)
    private val discount: View = itemView.findViewById(R.id.discount)
    private val image: ImageView = itemView.findViewById(R.id.image)

    fun bind(
        viewModel: FullItemViewModel,
        itemClickListener: OnItemClickListener
    ) {
        name.text = viewModel.collection
        manufacturer.text = viewModel.designer
        price.text = viewModel.currency + viewModel.price
        discount.visibility = if (viewModel.discount) View.VISIBLE else View.GONE
        val context = itemView.context
        val imageTag = viewModel.image
        try {
            val imageId = context.resources.getIdentifier(imageTag, "drawable", context.packageName)
            image.setImageResource(imageId)
        } catch (e: Throwable) {
            Log.d("", "")
        }
        itemView.setOnClickListener {
            itemClickListener.onItemClick(viewModel)
        }
    }

}

interface OnItemClickListener {
    fun onItemClick(model: FullItemViewModel)
}