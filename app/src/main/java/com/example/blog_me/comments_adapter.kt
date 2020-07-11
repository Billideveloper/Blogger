package com.example.blog_me

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class comments_adapter(val comments:ArrayList<blog_comments>, val commentlistner:commentoptionclicklistner): RecyclerView.Adapter<comments_adapter.viewholder>()
{

    inner class viewholder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){

        val commentusername = itemView?.findViewById<TextView>(R.id.commentlistusername)
        val commenttime = itemView?.findViewById<TextView>(R.id.commentlisttimestamp)
        val commenttext = itemView?.findViewById<TextView>(R.id.commentlistcommenttext)
        val commentoptions = itemView?.findViewById<ImageView>(R.id.commentoptionimage)




        fun bindcomment(comments: blog_comments){


            commentusername?.text = comments.username
            commenttext?.text = comments.commenttext

            val dateformatter = SimpleDateFormat("h:mm a, MM/d", Locale.getDefault())
            val datestring = dateformatter.format(comments.timestamp)

            commenttime?.text = datestring

            if(FirebaseAuth.getInstance().currentUser?.uid == comments.userId ){

                commentoptions?.visibility = View.VISIBLE
                commentoptions?.setOnClickListener {

                    commentlistner.commentoptiobclicked(comments)
                }

            }



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comments_list_view,parent, false)
        return viewholder(view)

    }

    override fun getItemCount(): Int {
        return comments.count()
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {

        holder.bindcomment(comments[position])

    }

}