package org.faclient

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import org.faclient.ui.theme.FireAlertClientTheme


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
    }
}