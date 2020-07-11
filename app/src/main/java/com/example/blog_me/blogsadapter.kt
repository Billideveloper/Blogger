package com.example.blog_me

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class blogsadapter(val blogs:ArrayList<Blog>, val bloglistner:blogoptionclicklistner,val itemclick: (Blog) -> Unit): RecyclerView.Adapter<blogsadapter.viewholder>()
{

    inner class viewholder(itemView: View?, val itemclick:(Blog) -> Unit) : RecyclerView.ViewHolder(itemView!!){

        val listviewblogname = itemView?.findViewById<TextView>(R.id.listviewblogname)
        val listviewblogtext = itemView?.findViewById<TextView>(R.id.listviewblogtext)
        val listviewtimestamp = itemView?.findViewById<TextView>(R.id.listviewtime)
        val listviewnumlikes = itemView?.findViewById<TextView>(R.id.listviewnumlikes)
        val listviewlikeimage = itemView?.findViewById<ImageView>(R.id.listviewlikebtn)
        val listviewlikebtn = itemView?.findViewById<ToggleButton>(R.id.listviewlikebtn2)
        val listviewnumcomments = itemView?.findViewById<TextView>(R.id.numcomment)
        val listviewblogoptions = itemView?.findViewById<ImageView>(R.id.blogoptionsmenu)
        val listviewblogusername = itemView?.findViewById<TextView>(R.id.blogusername)
        val listviewuseremail = itemView?.findViewById<TextView>(R.id.bloguseremail)


        fun bindblog(blog: Blog){

            listviewblogname?.text = blog.blogname

            listviewblogtext?.text = blog.blogtext

            listviewnumlikes?.text = blog.bloglike.toString()

            listviewnumcomments?.text = blog.numcomments.toString()

            listviewblogusername?.text = blog.username

            listviewuseremail?.text = blog.useremail


            if(FirebaseAuth.getInstance().currentUser?.uid == blog.userid){


                listviewblogoptions?.visibility = View.VISIBLE

                listviewblogoptions?.setOnClickListener {
                    bloglistner.blogsoptionsclicked(blog)
                }

            }



//
            val dateformatter = SimpleDateFormat("h:mm a, MM/d", Locale.getDefault())
            val datestring = dateformatter.format(blog.blogtime)

            listviewtimestamp?.text = datestring

            itemView.setOnClickListener { itemclick(blog) }

//            listviewlikeimage?.setOnClickListener {
//
//                FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blog.documentId).update(
//                    BLOGLIKE, blog.bloglike + 1)
//            }

            listviewlikebtn?.setOnCheckedChangeListener{buttonView, ischacked ->

                if(ischacked){
                    listviewlikeimage?.setBackgroundResource(R.drawable.unlikebtn)

                    FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blog.documentId).update(
                        BLOGLIKE, blog.bloglike + 1)

                }else{

                    listviewlikeimage?.setBackgroundResource(R.drawable.likebtn)


                    FirebaseFirestore.getInstance().collection(BLOGS_REF).document(blog.documentId).update(
                        BLOGLIKE, blog.bloglike - 1)
                }

            }



//

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_list_view,parent, false)
        return viewholder(view, itemclick)

    }

    override fun getItemCount(): Int {
       return blogs.count()
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {

        holder.bindblog(blogs[position])

    }

}