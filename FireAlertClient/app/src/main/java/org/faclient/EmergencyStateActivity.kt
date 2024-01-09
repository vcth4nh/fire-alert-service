package org.faclient

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.ncorti.slidetoact.SlideToActView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EmergencyStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_state)

        //TODO add time and location

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
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(1312)

                setEmergencyOff()
                finish()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setEmergencyOff() {
        val settingStorage = SettingStorage(application)
        GlobalScope.launch {
            settingStorage.stopEmergency()
            settingStorage.setLocation("")
            settingStorage.setTime(0)
        }
    }
}