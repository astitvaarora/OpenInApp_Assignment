package com.example.openinapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.openinapp.Models.link_model

class myViewHolder_link(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
    private val iconIv: ImageView = itemView.findViewById(R.id.linkphotoIV)
    private val linknameTv: TextView = itemView.findViewById(R.id.linknameTV)
    private val date: TextView = itemView.findViewById(R.id.date)
    private val no_of_click: TextView = itemView.findViewById(R.id.no_of_click_TV)
    private val link: TextView = itemView.findViewById(R.id.linkTV)

    fun bindData(user: link_model) {
        Glide.with(context)
            .load(user.pic)
            .override(180,180)
            .into(iconIv)
        linknameTv.text = user.link_name
        date.text = user.date
        no_of_click.text = user.no_of_click
        link.text = user.link
    }
}

class link_adapter(private val context: Context, private val data_ls: List<link_model>) : RecyclerView.Adapter<myViewHolder_link>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder_link {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.display_link_item, parent, false)
        return myViewHolder_link(view, context)
    }

    override fun getItemCount(): Int {
        return data_ls.size
    }

    override fun onBindViewHolder(holder: myViewHolder_link, position: Int) {
        val user = data_ls[position]
        holder.bindData(user)
    }
}

//
//class myViewHolder_link(itemView: View) : RecyclerView.ViewHolder(itemView){
//    private val iconIv : ImageView = itemView.findViewById(R.id.linkphotoIV)
//    private val linknameTv : TextView = itemView.findViewById(R.id.linknameTV)
//    private val date : TextView = itemView.findViewById(R.id.date)
//    private val no_of_click : TextView = itemView.findViewById(R.id.no_of_click_TV)
//    private val link : TextView = itemView.findViewById(R.id.linkTV)
//
//
//    fun bindData(user: link_model){
//        Glide.with(context)
//            .load(user.pic)
//            .into(iconIv)
//        linknameTv.text = user.link_name
//        date.text = user.date
//        no_of_click.text = user.no_of_click
//        link.text = user.link
//
//    }
//}
//
//class link_adapter(
//    private val data_ls: List<link_model>
//):RecyclerView.Adapter<myViewHolder_link>(){
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder_link {
//        val view = LayoutInflater
//            .from(parent.context)
//            .inflate(R.layout.display_link_item,parent,false)
//        return myViewHolder_link(view)
//    }
//
//    override fun getItemCount(): Int {
//
//        if(data_ls.size>=4){
//            return 4
//        }
//        return data_ls.size
//    }
//
//    override fun onBindViewHolder(holder: myViewHolder_link, position: Int) {
//        val user = data_ls[position]
//        holder.bindData(user)
//    }
//}