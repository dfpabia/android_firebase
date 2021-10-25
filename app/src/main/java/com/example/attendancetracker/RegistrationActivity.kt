package com.example.attendancetracker

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = Firebase.auth
        val db = Firebase.firestore
        val btnCancel: Button = findViewById(R.id.btnRegCancel)
        btnCancel.setOnClickListener {

            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            //     onBackPressed()
        }

        val btnRegister: Button = findViewById(R.id.btnRegRegister)
        btnRegister.setOnClickListener {

            val email: EditText = findViewById(R.id.txtRegEmail)
            val password: EditText = findViewById(R.id.txtRegPass)
            val lname: EditText = findViewById(R.id.txtRegLname)
            val fname: EditText = findViewById(R.id.txtRegFname)
            val bday: EditText = findViewById(R.id.txtRegBday)
            val gender: EditText = findViewById(R.id.txtRegGender)

            if(email.text.toString() == "" || password.text.toString() == "" || lname.text.toString() == "" || fname.text.toString() == ""
                || bday.text.toString() == "" || gender.text.toString() == ""){
                Toast.makeText(baseContext, "Incomplete Details",
                    Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success")
                         // val user = auth.currentUser

                            val userinfo = hashMapOf(
                                "firstName" to fname.text.toString(),
                                "lastName" to lname.text.toString(),
                                "email" to email.text.toString(),
                                "birthday" to bday.text.toString(),
                                "gender" to gender.text.toString()
                            )

                            // Add a new document with a generated ID
                            db.collection("users")
                                .add(userinfo)
                                .addOnSuccessListener { documentReference ->
                                   Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                    Toast.makeText(baseContext, "DocumentSnapshot added with ID: ${documentReference.id}",
                                        Toast.LENGTH_LONG).show()
                                    val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    auth.signOut()
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                   Log.w(TAG, "Error adding document", e)
                                    Toast.makeText(baseContext, "Error adding document: ${e}",
                                        Toast.LENGTH_LONG).show()

                                }


//                            Toast.makeText(baseContext, "createUserWithEmail:success",
//                                Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
//                            startActivity(intent)
//                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }

            }



        }
    }
}