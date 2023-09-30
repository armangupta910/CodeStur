package com.example.codestur

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.canteeniitj.Adaptor_For_Video_List
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class playlist_video_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_video)

        val name=intent.getStringExtra("Playlist Name") as String

        val data:MutableList<HashMap<String,String>> = mutableListOf()
        supportActionBar?.setTitle(name + " Playlist")
        supportActionBar?.setSubtitle("Codestur")
        val db=Firebase.firestore
        db.collection("CodeStur").get().addOnSuccessListener {
            for(i in it){
                if(i.id==name){
                    for(k in i.data){
                        if(k.key!="Tags" && k.key!="Banner"){
                            var hehe:HashMap<String,String>
                            hehe=k.value as HashMap<String, String>
                            hehe.put("Playlist",name)
                            data.add(hehe)
                        }

                    }
                }
            }
            val recycler = findViewById<RecyclerView>(R.id.recycler_for_playlist_video)
            val y = Adaptor_For_Video_List(this, data,0)
            recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recycler.adapter = y

            findViewById<SwipeRefreshLayout>(R.id.swipe_2).setOnRefreshListener {
                data.clear();
                y?.notifyDataSetChanged()
                db.collection("CodeStur").get().addOnSuccessListener {
                    for (i in it) {
                        if (i.id == name) {
                            for (k in i.data) {
                                if (k.key != "Tags" && k.key != "Banner") {
                                    var hehe: HashMap<String, String>
                                    hehe = k.value as HashMap<String, String>
                                    hehe.put("Playlist", name)
                                    data.add(hehe)
                                }

                            }
                        }
                    }
                    y?.notifyDataSetChanged()
                }
                Handler().postDelayed({
                    findViewById<SwipeRefreshLayout>(R.id.swipe_2).isRefreshing=false
                },1200)
            }
        }
    }
}