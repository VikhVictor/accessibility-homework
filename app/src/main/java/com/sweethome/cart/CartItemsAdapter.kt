package com.sweethome.cart

import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sweethome.R
import com.sweethome.item.FullItemViewModel


class CartItemsAdapter : RecyclerView.Adapter<CartItemViewHolder>() {

    val items = arrayListOf<FullItemViewModel>()

    fun updateList(list: List<FullItemViewModel>) {
        items.clear()
        items.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        return CartItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val item = items[position]

        holder.name.text = item.model
        holder.manufacturer.text = item.manufacturer
        holder.color.text = item.colorName
        holder.coloredCircle.backgroundTintList = ColorStateList.valueOf(item.color)
        val imageId = holder.imageId(item.image)
        holder.image.setImageResource(imageId)
    }
}

class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.name)
    val manufacturer: TextView = itemView.findViewById(R.id.manufacturer)
    val coloredCircle: View = itemView.findViewById(R.id.colored_circle)
    val color: TextView = itemView.findViewById(R.id.color)
    val image: ImageView = itemView.findViewById(R.id.image)

    fun resources(): Resources {
        return itemView.context.resources
    }

    fun imageId(image: String): Int {
        return resources().getIdentifier(image, "drawable", itemView.context.packageName)
    }
}