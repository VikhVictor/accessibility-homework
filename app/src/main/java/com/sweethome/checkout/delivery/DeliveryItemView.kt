package com.sweethome.checkout.delivery

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.sweethome.R

class DeliveryItemView(context: Context, attributeSet: AttributeSet?) :
    FrameLayout(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    var company: TextView
    var time: TextView
    var price: TextView
    var toggle: ImageView

    var chosen: Boolean = false

    var onChosenListener: OnChosenListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.delivery_type_item, this, true)

        company = findViewById(R.id.company)
        time = findViewById(R.id.time)
        price = findViewById(R.id.price)
        toggle = findViewById(R.id.toggle)
    }

    fun update(item: DeliveryViewModel) {
        company.text = item.name
        if (item.timeFrom == item.timeTo) {
            time.text = resources.getString(R.string.delivery_time)
        } else {
            time.text = resources.getString(R.string.time_from_to, item.timeFrom, item.timeTo)
        }
        price.text = item.price
        if (item.chosen)  {
            chosen = true
            toggle.setImageResource(R.drawable.ic_toggle_checked)
        } else {
            chosen = false
            toggle.setImageResource(R.drawable.ic_toggle_unchecked)
        }
        toggle.setOnClickListener {
            onChosenListener?.onItemChosen(item.id)
        }
    }
}

interface OnChosenListener {
    fun onItemChosen(id: String)
}
