package com.example.blog_me

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(),blogoptionclicklistner {


    var selectedCategory = TECH

    lateinit var blogsAdapter:blogsadapter

    val blogs = arrayListOf<Blog>()
    lateinit var auth: FirebaseAuth

    val blogcollectionref = FirebaseFirestore.getInstance().collection(BLOGS_REF)

    lateinit var blogslistner: ListenerRegistration

    //oncreate method which is called one time when application start this activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val bottomnavview = findViewById<BottomNavigationView>(R.id.bottomview)
//
//        val nav = findNavController(R.id.fragment)
//
//        bottomnavview.setupWithNavController(nav)

        fetchdata()
        adddivider()
        auth = FirebaseAuth.getInstance()


    }

    //everytime a users goes and comes backs the activity get resumed everytime so this onResume function is called on every resume

    override fun onResume() {

        super.onResume()
        setlistner()


        if(auth.currentUser == null){

//            Toast.makeText(this,"Please SignIn to Write Blog",Toast.LENGTH_LONG).show()
            Log.e("SignIn","Signin to add blog")


        }else{

            findViewById<Button>(R.id.addblogs).setOnClickListener { view ->

                var createblogintent = Intent(this,addblog::class.java)
                startActivity(createblogintent)
            }

        }

    }




    fun addblogclicked(view: View){

//        var createblogintent = Intent(this,com.example.blog_me.addblog::class.java)
//        startActivity(createblogintent)
        if(auth.currentUser == null){

            Toast.makeText(this,"Please SignIn to Write Blog",Toast.LENGTH_LONG).show()


        }
    }


    fun seeuserprofileclicked(view: View){

        var seeprofileintent = Intent(this,user_Profile::class.java)
        startActivity(seeprofileintent)
    }



    fun fetchdata(){



        blogsAdapter = blogsadapter(blogs,this){ blog ->



            val commentsactivity = Intent(this, comment::class.java)
            commentsactivity.putExtra(DOCUMENTID, blog.documentId)
            startActivity(commentsactivity)


//            findViewById<ImageView>(R.id.givecommentbtn).setOnClickListener { view ->
//
//                val commentsactivity = Intent(this, comment::class.java)
//                commentsactivity.putExtra(DOCUMENTID, blog.documentId)
//                startActivity(commentsactivity)
//
//            }
//
//
//            findViewById<TextView>(R.id.numcomment).setOnClickListener { view ->
////
//                val commentsactivity = Intent(this, comment::class.java)
//                commentsactivity.putExtra(DOCUMENTID, blog.documentId)
//                startActivity(commentsactivity)
//
//            }

        }

        bloglistVc.adapter = blogsAdapter

        val bloglayoutmanager = LinearLayoutManager(this)

        bloglistVc.layoutManager = bloglayoutmanager


        //fetch  blogs from firestore

//        blogcollectionref
//            .get()
//            .addOnSuccessListener { snapshot ->
//
//
//                for(document in snapshot.documents){
//
//                    val blogdata = document.data!!
//
//
////                    val timestamp: Timestamp? = document.getTimestamp("created at")
////                    val date = timestamp?.toDate()
////                    print(date)
//
//                        val blogname = blogdata[BLOGNAME] as String
//                        //val timestamp = blogdata[TIMESTAMP] as Date
//                        val blogtext = blogdata[BLOGTEXT] as String
//                        val blognumlikes = blogdata[BLOGLIKE] as Long
//                        val blognumcomments = blogdata[NUMCOMMENTS] as Long
//                        val documentid = document.id
//                        val time = blogdata[TIMESTAMP] as Date
//                        val newblog = Blog(time,blogname,blogtext,blognumlikes.toInt(),blognumcomments.toInt(),documentid)
//
//
//                        blogs.add(newblog)
//
//
//                }
//
//                blogsAdapter.notifyDataSetChanged()
//
//            }.addOnFailureListener { exception ->
//
//                Log.e("Exception", "could not fetch blogs: $exception")
//
//            }

    }


    fun setlistner(){

//        if(CATEGORY != selectedCategory){
//
//            Toast.makeText(this,"Sorry not available", Toast.LENGTH_SHORT).show()
//
//            }

            blogslistner = blogcollectionref
                .orderBy(TIMESTAMP,Query.Direction.DESCENDING)
                .whereEqualTo(CATEGORY,selectedCategory)
                .addSnapshotListener(this) { snapshot, exception ->

                    if (exception != null){

                        Log.e("Exception", "could not fetch blogs: $exception")

                    }

                    if(snapshot != null){

                        parsedata(snapshot)
                    }
                }


    }



    fun parsedata(snapshot: QuerySnapshot){

        blogs.clear()

        for(document in snapshot.documents){

            val blogdata = document.data!!

//                    val timestamp: Timestamp? = document.getTimestamp("created at")
//                    val date = timestamp?.toDate()
//                    print(date)

            val blogname = blogdata[BLOGNAME] as String
            //val timestamp = blogdata[TIMESTAMP] as Date
            val blogtext = blogdata[BLOGTEXT] as String
            val blognumlikes = blogdata[BLOGLIKE] as Long
            val blognumcomments = blogdata[NUMCOMMENTS] as Long
            val documentid = document.id
            val time = blogdata[TIMESTAMP] as Date
            val userid = blogdata[USERID] as String
            val username = blogdata[BLOGUSERNAME] as String
            val useremail = blogdata[BLOGUSEREMAIL] as String

            val newblog = Blog(time,blogname,blogtext,blognumlikes.toInt(),blognumcomments.toInt(),documentid,userid,username,useremail)

            blogs.add(newblog)


        }

        blogsAdapter.notifyDataSetChanged()
    }







    fun techcat(view: View){

        if (selectedCategory == TECH){
            techcatbtn.isChecked = true
            return
        }

        mediacatbtn.isChecked = false
        travelcatbtn.isChecked = false
        socialcatbtn.isChecked = false
        selectedCategory = TECH

        blogslistner.remove()
        setlistner()

    }


    fun travelcat(view: View){

        if (selectedCategory == TRAVEL){
            travelcatbtn.isChecked = true
            return
        }

        mediacatbtn.isChecked = false
        techcatbtn.isChecked = false
        socialcatbtn.isChecked = false

        selectedCategory = TRAVEL

        blogslistner.remove()
        setlistner()

    }

    fun meidacat(view: View){

        if (selectedCategory == MEDIA){
            mediacatbtn.isChecked = true
            return
        }

        travelcatbtn.isChecked = false
        techcatbtn.isChecked = false
        socialcatbtn.isChecked = false


        selectedCategory = MEDIA

        blogslistner.remove()
        setlistner()



    }




    fun socialcat(view: View){


        if (selectedCategory == SOCIAL){
            socialcatbtn.isChecked = true
            return
        }

        mediacatbtn.isChecked = false
        travelcatbtn.isChecked = false
        techcatbtn.isChecked = false



        selectedCategory = SOCIAL

        blogslistner.remove()
        setlistner()

    }

    override fun blogsoptionsclicked(blog: Blog) {


        if(FirebaseAuth.getInstance().currentUser?.uid == blog.userid){

            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.blogmenu,null)

            val deletbtn = dialogview.findViewById<Button>(R.id.deleteblog)

            builder.setView(dialogview).setNegativeButton("Cancel"){ _, _ ->}

            val ad = builder.show()

            deletbtn.setOnClickListener {

                //delet a comment
                val thoughtref =
                    FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blog.documentId)

                val collectionref = FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blog.documentId)
                    .collection(COMMENTSREF)

                deletblogcollection(collectionref,blog){sucess ->

                    if (sucess){

                        thoughtref.delete()
                            .addOnSuccessListener {

                                ad.dismiss()

                            }.addOnFailureListener { exception ->

                                Toast.makeText(this, "sorry cannot delet a blog", Toast.LENGTH_LONG).show()

                                ad.dismiss()
                            }
                    }
                }
            }

        }

        if(FirebaseAuth.getInstance().currentUser?.uid != blog.userid){


            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.blogmenu,null)

            val deletbtn = dialogview.findViewById<Button>(R.id.deleteblog)

            builder.setView(dialogview).setNegativeButton("Cancel"){ _, _ ->}

            deletbtn.text = "Report"

            val ad = builder.show()

            deletbtn.setOnClickListener {

                Toast.makeText(this,"Reported Sucessfully",Toast.LENGTH_LONG).show()

                ad.dismiss()

            }



        }



    }



    fun deletblogcollection(collection: CollectionReference,blog: Blog,complete: (Boolean) -> Unit){


        collection.get().addOnSuccessListener { snapshot ->

            thread {
                val batch = FirebaseFirestore.getInstance().batch()

                for (document in snapshot){

                    val docref = FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blog.documentId).collection(
                        COMMENTSREF).document(document.id)



                    batch.delete(docref)

                }
                batch.commit()
                    .addOnSuccessListener {
                        complete(true)
                    }
                    .addOnFailureListener { exception ->

                        Toast.makeText(this, "sorry cannot delet a blog", Toast.LENGTH_LONG).show()

                    }
            }
        }.addOnFailureListener {

            Toast.makeText(this, "sorry cannot delet a blog", Toast.LENGTH_LONG).show()

        }


    }


    fun adddivider(){

        val recyclerview = findViewById<RecyclerView>(R.id.bloglistVc)

        val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)

        decorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.recyclerview_divider)!!)

        recyclerview.addItemDecoration(decorator)


    }


}


