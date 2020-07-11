package com.example.blog_me

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_addblog.*
import kotlin.collections.HashMap


class addblog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addblog)

        progressbar.visibility = View.INVISIBLE

        cheackblog()
    }


    var selectedCategory = TECH




    fun cheackblog(){

        addblogs.isEnabled = false

        if (blogname.text == null){

            Log.e("Blog_Name", "Please give your Blog a name")


        }else{
            addblogs.isEnabled = true

        }

        if (blogtext.text == null){

            Log.e("Blog_Text", "Please give your Blog a text")

        }else{

            addblogs.isEnabled = true

        }

    }


    fun addtech(view: View){

        if (selectedCategory == TECH){
            techcategry.isChecked = true
            return
        }

        mediacategry.isChecked = false
        travelcategry.isChecked = false
        socialcategory.isChecked = false
        selectedCategory = TECH

    }

    fun addtravel(view: View){

        if (selectedCategory == TRAVEL){
            travelcategry.isChecked = true
            return
        }

        mediacategry.isChecked = false
        socialcategory.isChecked = false
        techcategry.isChecked = false

        selectedCategory = TRAVEL

    }

    fun addmedia(view: View){

        if (selectedCategory == MEDIA){
            mediacategry.isChecked = true
            return
        }

        travelcategry.isChecked = false
        socialcategory.isChecked = false
        techcategry.isChecked = false


        selectedCategory = MEDIA



    }

    fun addsocial(view: View){

        if (selectedCategory == SOCIAL){
            socialcategory.isChecked = true
            return
        }

        mediacategry.isChecked = false
        travelcategry.isChecked = false
        techcategry.isChecked = false


        selectedCategory = SOCIAL

    }


    fun addblog(view: View){

        val progressbar = findViewById<ProgressBar>(R.id.progressbar)

        progressbar.visibility = View.VISIBLE

        // add blog to firestore
        //create a hash map so that we can send all data in single event

       // val timestamp: Timestamp = snapshot.getTimestamp("created_at")
       // val date: Date = timestamp.toDate()

        val data = HashMap<String, Any>()
        data.put(CATEGORY, selectedCategory)
        data.put(NUMCOMMENTS,0)
        data.put(BLOGLIKE,0)
        data.put(BLOGNAME,blogname.text.toString())
        data.put(BLOGTEXT, blogtext.text.toString())
        data.put(TIMESTAMP,FieldValue.serverTimestamp())
        data.put(USERID,FirebaseAuth.getInstance().currentUser?.uid.toString())
        data.put(BLOGUSERNAME,FirebaseAuth.getInstance().currentUser?.displayName.toString())
        data.put(BLOGUSEREMAIL,FirebaseAuth.getInstance().currentUser?.email.toString())


        //sucessfully ceated a hash map of Data that we want to add to our firestore now let's put the data to firestore with two listners
        //addonsucesslistner and addonfailurelistner

        FirebaseFirestore.getInstance().collection(BLOGS_REF)
            .add(data)
            .addOnSuccessListener {
                //data added sucessfully to firestore now return to MainActivity of our application
                progressbar.visibility = View.INVISIBLE
                finish()
            }
            .addOnFailureListener { exception ->
                //something error happend try again
                Log.e("Exception","Could not share your blog Something error happend try again")
                progressbar.visibility = View.INVISIBLE
            }



    }







}