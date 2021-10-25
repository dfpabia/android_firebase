package com.example.attendancetracker

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserMaintenanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_maintenance)
        val db = Firebase.firestore

        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
//                   Log.d(TAG, "document ${document.id} => ${document.data.get("first")}")
                    addcard(document.id, document.data.get("firstName").toString()+" "+document.data.get("lastName").toString(), document.data.get("email").toString() )
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    private fun addcard(id:String, Cname:String, emailAdd:String) {
        var layout:LinearLayout = findViewById(R.id.container)
        var view: View = getLayoutInflater().inflate(R.layout.card, null)
        var email: TextView = view.findViewById(R.id.email)
        var name: TextView = view.findViewById(R.id.name)
        email.text = emailAdd
        name.text = Cname
        var btnDelete: Button = view.findViewById(R.id.delete)
        btnDelete.setOnClickListener {
            val db = Firebase.firestore
            db.collection("users").document(id)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!")
                layout.removeView(view)
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }


        }
        layout.addView(view)

    }


}

