package com.example.blog_me

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_comment.*
import java.util.*
import kotlin.collections.HashMap

class comment : AppCompatActivity(), commentoptionclicklistner {

    lateinit var blogDocumentId: String
    lateinit var commentsAdapter: comments_adapter

    val comments = arrayListOf<blog_comments>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        blogDocumentId = intent.getStringExtra(DOCUMENTID).toString()

       Log.d("e",blogDocumentId)

        commentsAdapter = comments_adapter(comments,this)
        commentlistview.adapter = commentsAdapter

        val layoutManager = LinearLayoutManager(this)

        commentlistview.layoutManager = layoutManager



        val progressbar = findViewById<ProgressBar>(R.id.progressB)
        progressbar.visibility = View.INVISIBLE


        loadcomments()

    }


    fun loadcomments(){

        FirebaseFirestore.getInstance().collection(BLOGS_REF)
            .document(blogDocumentId).collection(
            COMMENTSREF).orderBy(TIMESTAMP,Query.Direction.DESCENDING)
            .addSnapshotListener { Snapshot, exception ->

            if(exception != null){
                Log.e("Exception","could not retrieve comments")
            }

            if( Snapshot != null){

                comments.clear()

                for (document in Snapshot.documents){


                    val data = document.data!!
                    val name = data[USERNAME] as String
                    val time = data[TIMESTAMP] as Date
                    val commenttext = data[COMMENTTEXT] as String
                    val documentid = document.id
                    val userId = data[USERID] as String

                    val newcomment = blog_comments(name,time,commenttext,documentid,userId)

                    comments.add(newcomment)

                }

                commentsAdapter.notifyDataSetChanged()



            }

        }


    }


    fun sendcommentclicked(view: View){


        val progressbar = findViewById<ProgressBar>(R.id.progressB)
        progressbar.visibility = View.VISIBLE

        val commenttext = writecomment.text.toString()
        val blogref = FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blogDocumentId)

        FirebaseFirestore.getInstance().runTransaction { transaction ->
            val blog = transaction.get(blogref)
            val numcomments = blog.getLong(NUMCOMMENTS)!! + 1

            transaction.update(blogref, NUMCOMMENTS, numcomments)

            val newcommenttref = FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blogDocumentId)
                .collection(COMMENTSREF).document()

            val data = HashMap<String, Any>()

            data.put(COMMENTTEXT,commenttext)
            data.put(TIMESTAMP, FieldValue.serverTimestamp())
            data.put(USERNAME, FirebaseAuth.getInstance().currentUser?.displayName.toString())
            data.put(USERID, FirebaseAuth.getInstance().currentUser?.uid.toString())


            transaction.set(newcommenttref,data)
        }.
        addOnSuccessListener {

            writecomment.setText("")
            hidekeyboard()
            progressbar.visibility = View.INVISIBLE

        }
            .addOnFailureListener { exception ->

                progressbar.visibility = View.INVISIBLE

            Toast.makeText(this,"Sorry",Toast.LENGTH_LONG).show()
        }

    }


    private fun hidekeyboard(){

        val inputmanager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputmanager.isAcceptingText){
            inputmanager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }


    }



    //when comment option is clicked


    @SuppressLint("ResourceAsColor")
    override fun commentoptiobclicked(comment: blog_comments) {

        if (FirebaseAuth.getInstance().currentUser?.uid == comment.userId){


            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.optionsmenu,null)

            val deletbtn = dialogview.findViewById<Button>(R.id.optiondeletcomment)
            val editbtn = dialogview.findViewById<Button>(R.id.optioneditcomment)

            builder.setView(dialogview).setNegativeButton("Cancel"){ _, _ ->}

            val ad = builder.show()

            deletbtn.setOnClickListener {

                //delet a comment

                val progressbar = findViewById<ProgressBar>(R.id.progressB)

                progressbar.visibility = View.VISIBLE

                val commentref =
                    FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blogDocumentId)
                        .collection(
                            COMMENTSREF
                        ).document(comment.documentId)
                val thoughtref =
                    FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blogDocumentId)

//            commentref.delete().addOnSuccessListener {
//                ad.dismiss()
//
//            }.addOnFailureListener { exception ->
//
//                Toast.makeText(this,"sorry cannot delet a comment", Toast.LENGTH_LONG).show()
//                ad.dismiss()
//            }


                FirebaseFirestore.getInstance().runTransaction { transaction ->



                    val blog = transaction.get(thoughtref)

                    val numcomments = blog.getLong(NUMCOMMENTS)!! - 1

                    transaction.update(thoughtref, NUMCOMMENTS, numcomments)

                    transaction.delete(commentref)



                }.addOnSuccessListener {

                    progressbar.visibility = View.INVISIBLE
                    ad.dismiss()

                }.addOnFailureListener { exception ->
                    progressbar.visibility = View.INVISIBLE

                    Toast.makeText(this, "sorry cannot delet a comment", Toast.LENGTH_LONG).show()
                    ad.dismiss()
                }
            }


            editbtn.setOnClickListener {


                val updateintent = Intent(this,updatecomment::class.java)
                updateintent.putExtra(BlogdocID,blogDocumentId)
                updateintent.putExtra(CommentDocID,comment.documentId)
                updateintent.putExtra(Commnttextextra,comment.commenttext)
                ad.dismiss()

                startActivity(updateintent)

                //edit a comment

            }

        }else{

            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.optionsmenu,null)

            val deletbtn = dialogview.findViewById<Button>(R.id.optiondeletcomment)
            val editbtn = dialogview.findViewById<Button>(R.id.optioneditcomment)

            builder.setView(dialogview).setNegativeButton("Cancel"){ _, _ ->}

            val ad = builder.show()

            deletbtn.setText("Report Comment")

            deletbtn.setOnClickListener {

                Toast.makeText(this,"Comment Reported Sucessfully",Toast.LENGTH_LONG).show()

                ad.dismiss()
            }

            editbtn.setText("If you see somehting strange with this then please report so that we can analyze and can make it better for peoples like u")

            editbtn.setBackgroundColor(R.color.colorPrimaryDark)

            editbtn.textSize = 10F



        }




        //Toast.makeText(this,comment.commenttext,Toast.LENGTH_LONG).show()



    }



}