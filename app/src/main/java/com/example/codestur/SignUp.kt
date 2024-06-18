package com.example.codestur

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var token:SharedPreferences
    private lateinit var auth: FirebaseAuth

    private fun signInUsingCredentials(){
        val user=findViewById<EditText>(R.id.username).text.toString()
        val pass=findViewById<EditText>(R.id.password).text.toString()
        auth = Firebase.auth

        auth.signInWithEmailAndPassword(user, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Sign In Successfull",Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    val token=getSharedPreferences("email",Context.MODE_PRIVATE)
                    val editor=token.edit()
                    editor.putString("email","1")
                    editor.commit()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    auth.createUserWithEmailAndPassword(user, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this,"Sign Up Successfull",Toast.LENGTH_SHORT).show()
                                val token=getSharedPreferences("email",Context.MODE_PRIVATE)
                                val editor=token.edit()
                                editor.putString("email","")
                                editor.commit()
                                startActivity(Intent(this,MainActivity::class.java))
                                finish()
                            } else {


                                findViewById<Button>(R.id.enter).visibility=View.VISIBLE
                                findViewById<ProgressBar>(R.id.progress_signin).visibility=View.GONE
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this,"Password is wrong",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        token = getSharedPreferences("email", Context.MODE_PRIVATE)
        if(token.getString("email","")!=""){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.enter).setOnClickListener {
            findViewById<Button>(R.id.enter).visibility=View.GONE
            findViewById<ProgressBar>(R.id.progress_signin).visibility=View.VISIBLE
            signInUsingCredentials()
        }

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        googleSignInClient.revokeAccess()
        findViewById<ImageView>(R.id.google_sign).setOnClickListener {
            findViewById<ProgressBar>(R.id.progi).visibility=View.VISIBLE
            findViewById<LinearLayout>(R.id.screen).alpha=0.5F
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode == Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account:GoogleSignInAccount? = task.result
            if(account!=null){
                updateUI(account)
            }
        }
        else {
            Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show()
            findViewById<ProgressBar>(R.id.progi).visibility = View.GONE
            findViewById<LinearLayout>(R.id.screen).alpha = 1F
        }
    }
    private fun updateUI(account: GoogleSignInAccount){
        val cred=GoogleAuthProvider.getCredential(account.idToken,null)
        Firebase.auth.signInWithCredential(cred).addOnSuccessListener {
            var editor=token.edit()
            editor.putString("email","1")
            editor.commit()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }.addOnFailureListener {
            Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show()
            findViewById<ProgressBar>(R.id.progi).visibility=View.GONE
            findViewById<LinearLayout>(R.id.screen).alpha=1F
        }
    }
}