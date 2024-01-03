package org.faclient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoggedInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)
        val noFire = findViewById<ImageView>(R.id.no_fire)
        noFire.setImageResource(R.drawable.no_fire)

        val signOutButton = findViewById<Button>(R.id.sign_out_btn)
        signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivities(
                arrayOf(
                    Intent(this, LoginActivity::class.java),
                )
            )
            finish()
        }

        val dbRef = FirebaseDatabase.getInstance().reference
    }
}