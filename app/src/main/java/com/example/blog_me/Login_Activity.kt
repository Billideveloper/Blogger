package com.example.blog_me

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_.*

class Login_Activity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_)
        auth = FirebaseAuth.getInstance()


        val progressbar = findViewById<ProgressBar>(R.id.progressB)
        progressbar.visibility = View.INVISIBLE
    }





    //email and password are provided now login with email and password


    fun loginsigninclicked(view: View){

        val progressbar = findViewById<ProgressBar>(R.id.progressB)

        progressbar.visibility = View.VISIBLE

        val signinbutton = findViewById<Button>(R.id.loginsigninbtn)

        signinbutton.visibility = View.INVISIBLE

        val email = loginemailtext.text.toString()
        val password = loginpasswordtext.text.toString()

        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener { result ->

            val username = result.user!!.displayName.toString()

            Toast.makeText(this,username + "Sucessfully account created", Toast.LENGTH_LONG).show()

            finish()

        }.addOnFailureListener { exception ->

            Toast.makeText(this, "Sorry Something went wrong please try again", Toast.LENGTH_SHORT).show()

        }


    }



    //don't have account give a intent to create a account

    fun loginsignupclicked(view: View){


        val createuserintent = Intent(this,Register_Users::class.java)
        startActivity(createuserintent)

    }
}