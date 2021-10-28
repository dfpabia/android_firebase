package com.example.attendancetracker


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import java.util.*

class HomePageActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }
        auth = Firebase.auth
        firebaseAnalytics = Firebase.analytics

        val db = Firebase.firestore

        val welcomeMsg:TextView = findViewById(R.id.txtusername)
        val newString: String
        val extras = intent.extras
        if (extras == null) {
            newString = ""
        } else {
            newString = extras.getString("userdata").toString()
        }

        db.collection("users")
            .whereEqualTo("email", newString)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val year = Calendar.getInstance().get(Calendar.YEAR);
                    val strs = document.data.get("birthday").toString().split("/").toTypedArray()
                    val age = year.toInt()-strs[2].toInt()
                    firebaseAnalytics.setUserProperty("User_Age", age.toString())
                    firebaseAnalytics.setUserProperty("User_Gender", document.data.get("gender").toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


        welcomeMsg.text = "Welcome : " + newString
        val btnLogout = findViewById(R.id.btnLogout) as Button

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@HomePageActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
       //     onBackPressed()
        }

        val btnUserMaintenance:Button = findViewById(R.id.btnHomeUser)
        btnUserMaintenance.setOnClickListener {

            val intent = Intent(this@HomePageActivity, UserMaintenanceActivity::class.java)
            startActivity(intent)

            //     onBackPressed()
        }

        val btnHomeNews:Button = findViewById(R.id.btnHomeNews)
        btnHomeNews.setOnClickListener {
            Firebase.messaging.subscribeToTopic("daily_news")
                .addOnCompleteListener { task ->
                    var msg = getString(R.string.msg_subscribed)
                    if (!task.isSuccessful) {
                        msg = getString(R.string.msg_subscribe_failed)
                    }
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }
        }
    }

}