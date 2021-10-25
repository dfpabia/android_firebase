package com.example.attendancetracker

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if(currentUser != null){
//            Toast.makeText(this@MainActivity, "No User is logged In.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
            if (currentUser != null) {
                intent.putExtra("userdata", currentUser.getEmail())
            }
            startActivity(intent)
            finish()
        }

        val btnLogin = findViewById(R.id.button) as Button
        btnLogin.setOnClickListener {
//            ctr += 1
//            val tv = findViewById<View>(R.id.textView) as TextView
//            tv.text = "You clicked Me " + ctr
//            val params = Bundle()
//            params.putString("image_name", "clicked button")
//            params.putString("full_text", "Counter: "+ctr)
//            firebaseAnalytics.logEvent("Send_data", params)
                var email: EditText = findViewById(R.id.loginTxtEmail)
                var password: EditText = findViewById(R.id.loginTxtPassword)
                //email.text.toString()

                if(email.text.toString() == "" || password.text.toString() == ""){
                    Toast.makeText(baseContext, "Invalid Credential!",
                        Toast.LENGTH_SHORT).show()
                }else{
                    auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                // Log.d(TAG, "signInWithEmail:success")
                                var user = auth.currentUser
                                val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
                                if (user != null) {
                                    intent.putExtra("userdata", user.getEmail())
                                }

                                startActivity(intent)
                                finish()

                            } else {
                                // If sign in fails, display a message to the user.
                                // Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()

                            }
                        }
                }
        }

        val btnRegister = findViewById(R.id.btnRegister) as Button
        btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }





}