package com.example.codestur

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.canteeniitj.Adaptor_For_Video_List
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.core.View
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.net.InterfaceAddress


class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    @SuppressLint("RestrictedApi")

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }
    private lateinit var auth: FirebaseAuth
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val customMenuItem = menu?.findItem(R.id.profile)
        customMenuItem?.setOnMenuItemClickListener {
//            Toast.makeText(this,"Logged Out",Toast.LENGTH_SHORT).show()
//            val token=getSharedPreferences("email",Context.MODE_PRIVATE)
//            val editor=token.edit()
//            editor.putString("email","")
//            editor.commit()
//            auth = Firebase.auth
//            Firebase.auth.signOut()
//            startActivity(Intent(this,SignUp::class.java))
//            finish()
//            // Handle custom item click here
//            true
            showDialog()
            true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun showDialog(){

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val personPhoto: String = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        Log.d(TAG, personPhoto)
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
//            val personPhoto = acct.photoUrl
        }
        val dialog=Dialog(this)
        dialog.setContentView(R.layout.dialog)

        val profileImage:ImageView = dialog.findViewById<ImageView>(R.id.profileImage)

        val imageURL = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        Glide.with(this)
            .load(imageURL)
            .apply(RequestOptions().placeholder(R.drawable.person).error(R.drawable.person))
            .into(profileImage)


        dialog.findViewById<ImageView>(R.id.youtube).setOnClickListener {
//            Toast.makeText(this,"Youtube Clicked",Toast.LENGTH_SHORT).show()
            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/@Codestur")))
        }
        dialog.findViewById<ImageView>(R.id.instagram).setOnClickListener {
//            Toast.makeText(this,"Youtube Clicked",Toast.LENGTH_SHORT).show()
            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/armangupta910_____/?igshid=MzNlNGNkZWQ4Mg%3D%3D")))
        }
        dialog.findViewById<ImageView>(R.id.linkedin).setOnClickListener {
//            Toast.makeText(this,"Youtube Clicked",Toast.LENGTH_SHORT).show()
            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://www.linkedin.com/in/arman-gupta-67b91a172")))
        }
        dialog.findViewById<Button>(R.id.signout).setOnClickListener {
            Toast.makeText(this,"Logged Out",Toast.LENGTH_SHORT).show()
            val token=getSharedPreferences("email", Context.MODE_PRIVATE)
            val editor=token.edit()
            editor.putString("email","")
            editor.commit()
            auth = Firebase.auth
            Firebase.auth.signOut()
            startActivity(Intent(this,SignUp::class.java))
            finish()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.getWindow()?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        findViewById<FrameLayout>(R.id.frame).alpha=0.5F
//        dialog.findViewById<ImageView>(R.id.profile).setImageURI(personPhoto)
        dialog.show()
        dialog.setOnDismissListener {
            findViewById<FrameLayout>(R.id.frame).alpha=1F
        }
        dialog.findViewById<TextView>(R.id.goback).setOnClickListener {
            dialog.hide()
            findViewById<FrameLayout>(R.id.frame).alpha=1F
        }
        dialog.findViewById<TextView>(R.id.email).setText(FirebaseAuth.getInstance().currentUser?.email)
        dialog.findViewById<ProgressBar>(R.id.progress_email).isVisible=false
        dialog.findViewById<TextView>(R.id.email).isVisible=true
        dialog.findViewById<TextView>(R.id.playlist).setOnClickListener {
            dialog.hide()
            supportFragmentManager.beginTransaction().replace(R.id.frame,playlist_list_fragment(),"Current_Fragment").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
            findViewById<FrameLayout>(R.id.frame).alpha=1F
        }

        dialog.findViewById<TextView>(R.id.archived).setOnClickListener {
            dialog.hide()
            supportFragmentManager.beginTransaction().replace(R.id.frame,archived_fragment(),"Current_Fragment").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
            findViewById<FrameLayout>(R.id.frame).alpha=1F
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        supportActionBar?.setLogo(R.drawable.hehe)

        supportActionBar?.setTitle("Welcome Codestur")
        supportActionBar?.setSubtitle("Keep Coding")
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction().replace(R.id.frame,video_list_fragment(),"Current_Fragment").addToBackStack("Current_Fragment").commit()
//        Toast.makeText(this,"Fragment Set",Toast.LENGTH_SHORT).show()
        val bottom=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Home-> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, video_list_fragment(),"Current_Fragment")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                }
                R.id.Playlist->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,playlist_list_fragment(),"Current_Fragment").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                }
                R.id.Archived->supportFragmentManager.beginTransaction().replace(R.id.frame,archived_fragment(),"Current_Fragment").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
            }
            true
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}