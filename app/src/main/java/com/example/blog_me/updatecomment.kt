package com.example.blog_me

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import io.opencensus.stats.View
import kotlinx.android.synthetic.main.activity_updatecomment.*

class updatecomment : AppCompatActivity() {


  lateinit var blogid: String

    lateinit var commentid: String

    lateinit var commenttext: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updatecomment)

        blogid = intent.getStringExtra(BlogdocID).toString()
        commentid = intent.getStringExtra(CommentDocID).toString()
        commenttext = intent.getStringExtra(Commnttextextra).toString()

        updatecmnttext.setText(commenttext)


        updatecomment.setOnClickListener {
            updatecmentclicked()
        }

    }

    
    fun updatecmentclicked(){


        val updatedcmmnt = updatecmnttext.text.toString()

        FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blogid).collection(
            COMMENTSREF).document(commentid)
            .update(COMMENTTEXT,updatedcmmnt)
            .addOnSuccessListener {
                hidekeyboard()
                finish()
            }
            .addOnFailureListener { exception ->

                Toast.makeText(this,"sorry could not update a comment", Toast.LENGTH_LONG).show()
            }


    }


    private fun hidekeyboard(){

        val inputmanager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputmanager.isAcceptingText){
            inputmanager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }


    }
}
