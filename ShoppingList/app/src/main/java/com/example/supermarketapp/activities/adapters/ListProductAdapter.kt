package com.example.supermarketapp.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.supermarketapp.R
import com.example.supermarketapp.activities.dataclass.Product
import com.example.supermarketapp.activities.holders.ListProductHolder

class ListProductAdapter(private val listP:MutableList<Product>,
                         private val onClickDelete: (Int, String) -> Unit
): RecyclerView.Adapter<ListProductHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProductHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_products, parent,
            false)

        return ListProductHolder(view)
    }

    override fun onBindViewHolder(holder: ListProductHolder, position: Int) {
        holder.render(listP[position], onClickDelete)
    }

    override fun getItemCount(): Int = listP.size

}