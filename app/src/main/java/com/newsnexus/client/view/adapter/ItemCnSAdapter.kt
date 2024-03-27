package com.newsnexus.client.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsnexus.client.R
import com.newsnexus.client.databinding.ItemCnsLayoutBinding
import com.newsnexus.client.model.dataclass.dummy.CritiqueSuggestions

class ItemCnSAdapter(
    var data: MutableList<CritiqueSuggestions>
): RecyclerView.Adapter<ItemCnSAdapter.ItemHolder>() {
    inner class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ItemCnsLayoutBinding.bind(itemView)

        fun bind (item: CritiqueSuggestions) = with(itemView){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cns_layout, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(data.get(position))
    }

    fun updateItem(listCnS: List<CritiqueSuggestions>){
        try {
            if(listCnS != null){
                data.clear()
                data.addAll(listCnS)
                notifyDataSetChanged()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}