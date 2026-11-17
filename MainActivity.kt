package com.example.voicewriter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pad = (16 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(pad, pad * 2, pad, pad) }

        fun label(t: String) = TextView(this).apply { text = t; textSize = 16f; setPadding(0, pad, 0, pad / 2) }
        fun button(t: String, onClick: () -> Unit) = Button(this).apply { text = t; setOnClickListener { onClick() } }

        root.addView(TextView(this).apply { text = "AI Voice Writer"; textSize = 24f })
        root.addView(TextView(this).apply {
            text = "বাংলা টাইপিং ফোনেটিক (Avro-র মতো): ami → আমি, tumi → তুমি, kolom → কলম, bhalO → ভালো। " +
                "বড় হাতের অক্ষর (Shift): T=ট D=ড N=ণ R=ড় S=শ Sh=ষ O=ো। " +
                "কিবোর্ডে \"বাং/EN\" চেপে ভাষা বদলান, 🎤 চেপে ভয়েস, 🌐 চেপে অন্য কিবোর্ড।"
            textSize = 14f
            setPadding(0, pad, 0, 0)
        })
        root.addView(label("ধাপ ১: Gemini API Key বসান (aistudio.google.com থেকে ফ্রিতে নিন)"))
        val keyBox = EditText(this).apply {
            hint = "API Key"
            setText(getSharedPreferences("p", MODE_PRIVATE).getString("key", ""))
        }
        root.addView(keyBox)
        root.addView(button("Key সেভ করুন") {
            getSharedPreferences("p", MODE_PRIVATE).edit().putString("key", keyBox.text.toString().trim()).apply()
            Toast.makeText(this, "সেভ হয়েছে", Toast.LENGTH_SHORT).show()
        })
        root.addView(label("ধাপ ২: মাইক পারমিশন দিন"))
        root.addView(button("মাইক পারমিশন") { requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1) })
        root.addView(label("ধাপ ৩: কিবোর্ড চালু করুন (তালিকায় AI Voice Writer অন করুন)"))
        root.addView(button("কিবোর্ড সেটিংস খুলুন") { startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) })
        root.addView(label("ধাপ ৪: কিবোর্ড হিসেবে বেছে নিন"))
        root.addView(button("কিবোর্ড বদলান") {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
        })
        setContentView(root)
    }
}
