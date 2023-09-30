package com.example.codestur

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.canteeniitj.Adaptor_For_Video_List
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class archived_fragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val x=inflater.inflate(R.layout.fragment_archived_fragment, container, false)
        val recycler:RecyclerView = x.findViewById(R.id.video_list_archive)
        val db=Firebase.firestore
        var data:MutableList<HashMap<String,String>> = mutableListOf()
        db.collection("Archived").get().addOnSuccessListener {
            for(i in it){
                if(i.id==FirebaseAuth.getInstance().currentUser?.uid.toString()){
                    data=i.data.get("Archived") as MutableList<HashMap<String, String>>
                }
            }
            val y = context?.let { Adaptor_For_Video_List(it, data,1) }
            recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recycler.adapter = y
            x.findViewById<SwipeRefreshLayout>(R.id.frame_archive_fragment).setOnRefreshListener {
                data.clear()
                y?.notifyDataSetChanged()
                var data1:MutableList<HashMap<String,String>> = mutableListOf()
                db.collection("Archived").get().addOnSuccessListener {
                    for (i in it) {
                        if(i.id==FirebaseAuth.getInstance().currentUser?.uid){
                            data1=i.data.get("Archived") as MutableList<HashMap<String, String>>
                        }
                    }
                    for(i in data1){
                        data.add(i)
                    }
                    y?.notifyDataSetChanged()
                }

                Handler().postDelayed({
                    x.findViewById<SwipeRefreshLayout>(R.id.frame_archive_fragment).isRefreshing =
                        false
                }, 1200)
            }

        }

        return x
    }
}