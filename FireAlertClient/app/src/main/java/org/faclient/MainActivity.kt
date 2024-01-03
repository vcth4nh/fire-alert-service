package org.faclient

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            startActivities(
                arrayOf(
                    Intent(this, LoggedInActivity::class.java),
                )
            )
        } else {
            startActivities(
                arrayOf(
                    Intent(this, LoginActivity::class.java),
                )
            )
        }
        finish()
    }
}