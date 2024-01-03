package org.faclient

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity

class LoggedInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)
        val noFire = findViewById<ImageView>(R.id.no_fire)
        noFire.setImageResource(R.drawable.no_fire)

    }
}