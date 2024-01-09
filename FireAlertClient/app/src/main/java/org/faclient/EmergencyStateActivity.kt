package org.faclient

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.ncorti.slidetoact.SlideToActView

class EmergencyStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_state)

        val emergencyCallBtn = findViewById<ImageButton>(R.id.emergency_call)
        emergencyCallBtn.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.setData(Uri.parse("tel:114"))
            startActivity(callIntent)
            finish()
        }

        val slider = findViewById<SlideToActView>(R.id.confirm_swipe_btn)
        slider.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                finish()
            }
        }
    }
}