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
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codestur.R
import com.example.codestur.Video_Player
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Adaptor_For_Video_List(val conti:Context,val data:MutableList<HashMap<String,String>>,val ref:Int):RecyclerView.Adapter<Adaptor_For_Video_List.view_holder>() {
    inner class view_holder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val tags: TextView
        val playlist: TextView
        val thumbnail: ImageView
        val khali: View
        val clickable: LinearLayout
        val menu: ImageView

        init {
            title= itemView.findViewById<TextView>(R.id.video_name)
            tags= itemView.findViewById<TextView>(R.id.video_tags)
            playlist= itemView.findViewById<TextView>(R.id.video_playlist)
            thumbnail= itemView.findViewById<ImageView>(R.id.thumbnail_image)
            khali= itemView.findViewById<View>(R.id.khali_view)
            clickable = itemView.findViewById<LinearLayout>(R.id.clickable_video)
            menu= itemView.findViewById(R.id.menu)
            menu.setOnClickListener {
                val x = PopupMenu(conti,it)
                x.inflate(R.menu.option_menu)

                x.setOnMenuItemClickListener{
                    when(it.itemId){
                        R.id.notes -> {
                            true
                        }
                        R.id.archive -> {
                            val db=Firebase.firestore
                            db.collection("Archived").get().addOnSuccessListener {
                                var k=0
                                for(i in it){
                                    if(i.id == FirebaseAuth.getInstance().currentUser?.uid.toString()){
                                        k=1
                                        break
                                    }
                                }
                                if(k==0){
                                    var x:HashMap<String,MutableList<HashMap<String,String>>>
                                    var list:MutableList<HashMap<String,String>> = mutableListOf()
                                    x= hashMapOf("Archived" to list)
                                    db.collection("Archived").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).set(x)
                                }
                                    var x:HashMap<String,MutableList<HashMap<String,String>>>
                                    var list:MutableList<HashMap<String,String>> = mutableListOf()
                                    db.collection("Archived").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).get().addOnSuccessListener{
                                        list=it.get("Archived") as MutableList<HashMap<String, String>>
                                        var a=1
                                        for(i in list){
                                            if(i.get("Title")==data[position].get("Title")){
                                                a=0
                                                Toast.makeText(conti, "Already Archived", Toast.LENGTH_SHORT).show()
                                                break
                                            }
                                        }
                                        if(a==1){
                                            list.add(data[position])
                                            db.collection("Archived").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).update("Archived",list)
                                        }

                                    }
                            }
                            true
                        }
                        else->true
                    }
                }

                x.show()
                val y=PopupMenu::class.java.getDeclaredField("mPopup")
                y.isAccessible=true
                val menu=y.get(x)
                menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java).invoke(menu,true)
            }
        }
        val notes=itemView.findViewById<Button>(R.id.download_button)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): view_holder {
        var itemView:View=LayoutInflater.from(parent.context).inflate(R.layout.card_for_video,parent,false)
        return view_holder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: view_holder, position: Int) {
        val x=AlphaAnimation(1F,0.4F)

        holder.clickable.setOnClickListener{
            holder.clickable.startAnimation(x)
            val intent=Intent(conti,Video_Player::class.java)
            intent.putExtra("Link",data[position].get("Video_Link").toString())
            conti.startActivity(intent)
            Toast.makeText(conti,"Clicked",Toast.LENGTH_SHORT).show()
        }
        holder.title.setText(data[position].get("Title"))
        holder.playlist.setText(data[position].get("Playlist"))
        holder.tags.setText(data[position].get("Tags"))
//
        if(ref==1){
            holder.menu.visibility=View.GONE
        }
        Glide.with(conti).load(data[position].get("Thumbnail_Link")).into(holder.thumbnail)
        holder.khali.visibility=View.GONE
        holder.thumbnail.visibility=View.VISIBLE
    }
}