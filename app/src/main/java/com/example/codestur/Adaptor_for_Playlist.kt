package com.example.canteeniitj

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codestur.R
import com.example.codestur.Video_Player
import com.example.codestur.playlist_video_activity

class Adaptor_for_Playlist(val conti:Context,val data:MutableList<HashMap<String,String>>):RecyclerView.Adapter<Adaptor_for_Playlist.view_holder>() {
    class view_holder(itemView: View):RecyclerView.ViewHolder(itemView){
        val banner=itemView.findViewById<ImageView>(R.id.banner)
        val title=itemView.findViewById<TextView>(R.id.title)
        val tags=itemView.findViewById<TextView>(R.id.tags_playlist)
        val number=itemView.findViewById<TextView>(R.id.number_of_videos)
        val click=itemView.findViewById<LinearLayout>(R.id.playlist_click)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): view_holder {
        var itemView:View=LayoutInflater.from(parent.context).inflate(R.layout.playlist_card,parent,false)
        return view_holder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: view_holder, position: Int) {
        holder.title.setText(data[position].get("Title"))
        holder.tags.setText(data[position].get("Tags"))
        holder.number.setText(data[position].get("Total") + " videos")
        Glide.with(conti).load(data[position].get("Banner")).into(holder.banner)

        holder.click.setOnClickListener {
            val intent=Intent(conti,playlist_video_activity::class.java)
            intent.putExtra("Playlist Name",data[position].get("Title"))
            conti.startActivity(intent)
        }
    }
}