package com.shenhua.weather

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.include_aqi.*
import kotlinx.android.synthetic.main.include_forecast.*
import kotlinx.android.synthetic.main.include_now.*
import kotlinx.android.synthetic.main.include_suggestion.*
import kotlinx.android.synthetic.main.include_title.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 * Created by shenhua on 2018-03-22-0022.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val v = window.decorView
            v.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)
        loadBingImg()
        nav_button.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        val weatherId = defaultSharedPreferences.getString("weather_id", "")
        weather_layout.visibility = View.INVISIBLE
        requestWeather(weatherId)
        swipe_refresh.setOnRefreshListener {
            requestWeather(defaultSharedPreferences.getString("weather_id", ""))
        }
    }

    private fun loadBingImg() {
        val url = Constant.PIC
        async {
            val s = URL(url).readText()
            uiThread {
                Glide.with(this@WeatherActivity).load(s).into(bing_pic_img)
            }
        }
    }

    fun requestWeather(wid: String) {
        val url = "${Constant.WEATHER}?cityid=${wid}&key=${Constant.KEY}"
        doAsync {
            val s = URL(url).readText()

            uiThread {
                val weather = Gson().fromJson(s, Weather::class.java)
                swipe_refresh.isRefreshing = false
                showWeatherInfo(weather.HeWeather[0])
            }
        }
    }

    private fun showWeatherInfo(w: HeWeather) {
        if (w.status == "unknown city") {
            return
        }

        title_city.text = w.basic.city
        title_update_time.text = w.basic.update.loc
        degree_text.text = "${w.now.tmp} ℃"
        weather_info_text!!.text = "${w.now.cond.txt}     ${w.now.wind_dir}  ${w.now.wind_sc}级"
        forecast_layout!!.removeAllViews()
        for (d in w.daily_forecast) {
            val v = LayoutInflater.from(this).inflate(R.layout.item_forecast, forecast_layout, false)
            val dateText = v.find<TextView>(R.id.date_text)
            val infoText = v.find<TextView>(R.id.info_text)
            val maxText = v.find<TextView>(R.id.max_text)
            val minText = v.find<TextView>(R.id.min_text)
            dateText.text = d.date
            maxText.text = d.tmp.max
            minText.text = d.tmp.min
            if (d.cond.code_d == d.cond.code_n) {
                infoText.text = d.cond.txt_d
            } else {
                infoText.text = d.cond.txt_d + "->" + d.cond.txt_n
            }
            forecast_layout!!.addView(v)
        }
        aqi_text!!.text = w.aqi.city.aqi
        pm25_text!!.text = w.aqi.city.pm25
        comfort_text!!.text = "舒适度：${w.suggestion.comf.txt}"
        car_wash_text!!.text = "洗车指数：${w.suggestion.cw.txt}"
        sport_text!!.text = "运动建议：${w.suggestion.sport.txt}"
        weather_layout!!.visibility = View.VISIBLE
    }
}