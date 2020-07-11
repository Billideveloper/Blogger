package com.example.blog_me

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_user__profile.*
import java.util.*

class user_Profile : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var Userslistner: ListenerRegistration
    val Userref = FirebaseFirestore.getInstance().collection(USERS_REF)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__profile)



        auth = FirebaseAuth.getInstance()

        findViewById<ImageView>(R.id.profileimage).setOnClickListener { view ->


            val photointent = Intent(Intent.ACTION_PICK)
            photointent.type = "image/="
            startActivityForResult(photointent,0)

//            val updatebtn: View = findViewById<Button>(com.example.blog_me.R.id.updateprofile)
//            updatebtn.visibility = View.VISIBLE


        }
    }

    var selectedphotouri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            val updatebtn: View = findViewById<Button>(com.example.blog_me.R.id.updateprofile)
            updatebtn.visibility = View.VISIBLE

            selectedphotouri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedphotouri)
            val bitmapdrable = BitmapDrawable(bitmap)

            profileimage.setBackgroundDrawable(bitmapdrable)


        }

    }


    fun updateprofileclicked(view: View){


        val filename = UUID.randomUUID().toString()




    }

    override fun onResume() {
        super.onResume()

        if(auth.currentUser == null){
            //user is logged out

            signoutbtn.text = "SignIn"
            profileimage.isEnabled = false

            profilename.text = "SORRY"
            profileemail.text = "Please SignIn to See Your profile"


            findViewById<Button>(R.id.signoutbtn).setOnClickListener { view ->
                val signinintent = Intent(this,Login_Activity::class.java)
                startActivity(signinintent)
            }



        }else{

            fetchuser()
            profilename.text = FirebaseAuth.getInstance().currentUser?.displayName.toString()
            profileemail.text = FirebaseAuth.getInstance().currentUser?.email.toString()

            signoutbtn.text = "SignOut"
            //user is Logeed In

            profileimage.isEnabled = true

            findViewById<Button>(R.id.signoutbtn).setOnClickListener { view ->

                FirebaseAuth.getInstance().signOut()
                
                Toast.makeText(this,"Sucessfully SignOut", Toast.LENGTH_LONG).show()

                val signinintent = Intent(this,Login_Activity::class.java)
                startActivity(signinintent)

            }



        }
    }


    fun fetchuser(){

        Userslistner = Userref.addSnapshotListener(this) { snapshot, exception ->


            if (exception != null){

                Log.e("Exception", "could not fetch blogs: $exception")

            }

            if(snapshot != null){

                parsedata(snapshot)
            }

        }




    }

    fun parsedata(snapshot: QuerySnapshot){

        for(document in snapshot.documents){

            val userdata = document.data!!


        }
    }


    fun signoutbutnclickedd(view: View) {



    }
}