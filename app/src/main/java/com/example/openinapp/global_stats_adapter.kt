package com.example.openinapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.openinapp.Models.global_starts_model

class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val iconIv : ImageView = itemView.findViewById(R.id.card_icon_IV)
    private val dataTV : TextView = itemView.findViewById(R.id.dataTV)
    private val dataTtileTV : TextView = itemView.findViewById(R.id.data_titleTV)

    fun bindData(user: global_starts_model,icon:Int ){
        iconIv.setImageResource(icon)
        dataTV.text = user.data
        dataTtileTV.text = user.data_title
    }
}

class global_stats_adapter(
    private val data_ls: List<global_starts_model>
):RecyclerView.Adapter<myViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.global_stats_item,parent,false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data_ls.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val user = data_ls[position]
        val ls = listOf<Int>(
            R.drawable.today_click_icon,
            R.drawable.location_icon,
            R.drawable.web_icon,
            R.drawable.time_icon
        )
        val icon = ls[position]
        holder.bindData(user,icon)
    }
}