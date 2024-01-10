package org.faclient

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ncorti.slidetoact.SlideToActView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EmergencyStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_state)
        setLocationAndTime()
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

    private fun setEmergencyOff() {
        val settingStorage = SettingStorage(application)
        CoroutineScope(Dispatchers.IO).launch {
            settingStorage.stopEmergency()
            settingStorage.setLocation("")
            settingStorage.setTime(0)
        }
    }

    private fun setLocationAndTime() {
        val locationText = findViewById<TextView>(R.id.emergency_location)
        val timeText = findViewById<TextView>(R.id.emergency_time)
        val settingStorage = SettingStorage(application)

        CoroutineScope(Dispatchers.IO).launch {
            settingStorage.getLocation.collectLatest {
                locationText.text = it
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            settingStorage.getTime.collectLatest { time ->
                run {
                    val sdf = SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US)
                    val netDate = Date(time * 1000)
                    val timeStr = sdf.format(netDate)
                    timeText.text = timeStr
                }
            }
        }
    }

}