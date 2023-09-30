package com.example.codestur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        findViewById<TextView>(R.id.signintosignup).setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }
    }
}