package com.example.codestur

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.canteeniitj.Adaptor_For_Video_List
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class video_list_fragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val x = inflater.inflate(R.layout.fragment_video_list, container, false)
        val recycler = x.findViewById<RecyclerView>(R.id.video_list_recycle)
//        Toast.makeText(context,"Recycler Initiated",Toast.LENGTH_SHORT).show()
        //Getting Data
        val data: MutableList<HashMap<String, String>> = mutableListOf()
        val db = Firebase.firestore
//        Toast.makeText(context,"Database Initiated",Toast.LENGTH_SHORT).show()
        db.collection("CodeStur").get().addOnSuccessListener {
            for (i in it) {
                for (j in i.data) {
                    val x: HashMap<String, String>
                    if (j.key != "Tags" && j.key != "Banner") {
                        x = j.value as HashMap<String, String>
                        x["Playlist"] = i.id
                        data.add(x)
                    }
                }
            }
//            Toast.makeText(context,"Data Received",Toast.LENGTH_SHORT).show()
            val y = context?.let { Adaptor_For_Video_List(it, data,0) }
            recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recycler.adapter = y
            x.findViewById<SwipeRefreshLayout>(R.id.frame_video_fragment).setOnRefreshListener {
                data.clear()
                y?.notifyDataSetChanged()

                db.collection("CodeStur").get().addOnSuccessListener {
                    for (i in it) {
                        for (j in i.data) {
                            val x: HashMap<String, String>
                            if (j.key != "Tags" && j.key != "Banner") {
                                x = j.value as HashMap<String, String>
                                x["Playlist"] = i.id
                                data.add(x)
                            }
                        }
                    }
                    y?.notifyDataSetChanged()
                }

                Handler().postDelayed({
                    x.findViewById<SwipeRefreshLayout>(R.id.frame_video_fragment).isRefreshing =
                        false
                }, 1200)
            }

        }
//        Toast.makeText(context,"Lafde",Toast.LENGTH_SHORT).show()
        //Getting Data Finished
        return x
    }
}