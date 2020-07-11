package com.example.blog_me

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register__users.*

class Register_Users : AppCompatActivity() {


    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register__users)
        auth = FirebaseAuth.getInstance()
    }



    //user sucessfully filled all fields now add all this details to database and create a user


    fun createusersignupbtnclicked(view: View){


        var name = createusername.text.toString()
        var email = createuseremail.text.toString()
        var password = createuserpassword.text.toString()
        var phoneno = createuserphoneno.text.toString()
        var birthdate = createuserbirthdate.text.toString()


        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->

            Toast.makeText(this,"Your account sucessfully created ", Toast.LENGTH_LONG).show()


            val user = FirebaseAuth.getInstance().currentUser

            val userdata = HashMap<String, Any>()
            userdata.put(USERNAME, name)
            userdata.put(USEREMAIL,email)
            userdata.put(USERBIRTHDATE,birthdate)
            userdata.put(USERPHONENO,phoneno)
            userdata.put(USERID, user!!.uid.toString())
            userdata.put(TIMESTAMP, FieldValue.serverTimestamp())

//            val profileUpdates = UserProfileChangeRequest.Builder()
//                .setDisplayName("Jane Q. User")
//                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                .build()

            val profileupdate = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            user.updateProfile(profileupdate).addOnFailureListener { exception ->
                Toast.makeText(this,"Your account not updated ", Toast.LENGTH_LONG).show()
            }

                val useruid = result.user!!.uid.toString()

            FirebaseFirestore.getInstance().collection(USERS_REF).document(useruid).set(userdata).addOnSuccessListener {
                finish()
            }.addOnFailureListener { exception ->

                Toast.makeText(this,"Your account not updated something went wrong try again ", Toast.LENGTH_LONG).show()

            }


        }


            .addOnFailureListener { exception ->



            Toast.makeText(this,"Your account is not created please try after some time ", Toast.LENGTH_LONG).show()

        }



    }






    //user allready have account take them back to login screen


    fun createusersigninclicked(view: View){


        finish()

    }
}