package com.shenhua.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val s = defaultSharedPreferences.getString("weather_id", "")
        if (!TextUtils.isEmpty(s)) {
            startActivity<WeatherActivity>()
            finish()
        }
    }
}
