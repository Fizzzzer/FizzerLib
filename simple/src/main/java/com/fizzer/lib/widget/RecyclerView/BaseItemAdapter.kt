package com.fizzer.lib.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.fizzer.lib.R

class BaseItemAdapter : Adapter<BaseItemAdapter.BaseViewHolder>() {
    var data = mutableListOf<ItemData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class BaseViewHolder(itemView: View) : ViewHolder(itemView) {
        var textView = itemView.findViewById<AppCompatTextView>(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_layout, parent,false)
        return BaseViewHolder(itemView)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.textView.text = data[position].title
    }
}