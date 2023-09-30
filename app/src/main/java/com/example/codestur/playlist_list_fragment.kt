package com.example.codestur

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.canteeniitj.Adaptor_for_Playlist
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class playlist_list_fragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val x=inflater.inflate(R.layout.fragment_playlist_list_fragment, container, false)

        val db=Firebase.firestore
        db.collection("CodeStur").get().addOnSuccessListener {
            val data:MutableList<HashMap<String,String>> = mutableListOf()
            for(i in it){
                val hashi = HashMap<String,String>()
                hashi["Title"]=i.id
                var total=0
                for(j in i.data){
                    total++
                    if(j.key=="Banner"){
                        val new:HashMap<String,String>
                        new=j.value as HashMap<String, String>
                        hashi["Banner"] = new.get("Banner") as String
                    }
                    if(j.key=="Tags"){
                        val new:HashMap<String,String>
                        new=j.value as HashMap<String, String>
                        hashi["Tags"] = new.get("Tags") as String
                    }
                }
                hashi["Total"]=(total-2).toString()
                data.add(hashi)
            }
            val a=x.findViewById<RecyclerView>(R.id.recycler_for_playlist)
            val b= context?.let { it1 -> Adaptor_for_Playlist(it1,data) }
            a.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            a.adapter=b

            x.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_playlist).setOnRefreshListener {
                data.clear()
                b?.notifyDataSetChanged()
                db.collection("CodeStur").get().addOnSuccessListener {
                    for (i in it) {
                        val hashi = HashMap<String, String>()
                        hashi["Title"] = i.id
                        var total = 0
                        for (j in i.data) {
                            total++
                            if (j.key == "Banner") {
                                val new: HashMap<String, String>
                                new = j.value as HashMap<String, String>
                                hashi["Banner"] = new.get("Banner") as String
                            }
                            if (j.key == "Tags") {
                                val new: HashMap<String, String>
                                new = j.value as HashMap<String, String>
                                hashi["Tags"] = new.get("Tags") as String
                            }
                        }
                        hashi["Total"] = (total - 2).toString()
                        data.add(hashi)
                    }
                    x.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_playlist).isRefreshing=false
                    b?.notifyDataSetChanged()
                }
            }

        }

        return x
    }

}