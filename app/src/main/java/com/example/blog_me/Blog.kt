package com.example.blog_me

import android.widget.ImageView
import java.util.*


data class Blog constructor(val blogtime: Date,val blogname: String, val blogtext: String, val bloglike: Int, val numcomments: Int, val documentId: String, val userid: String, val username: String, val useremail:String)