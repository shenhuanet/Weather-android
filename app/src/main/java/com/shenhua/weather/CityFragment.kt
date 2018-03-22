package com.shenhua.weather

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.frag_city.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.uiThread
import java.net.URL
import java.util.*

/**
 * Created by shenhua on 2018-03-22-0022.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class CityFragment : Fragment() {

    private var currentLevel: Int = 0
    private var provinceList = ArrayList<Province>()
    private var cityList = ArrayList<Citys>()
    private var countyList = ArrayList<County>()

    private var selectedProvince: Province? = null
    private var selectedCity: Citys? = null
    private var selectedCounty: County? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.frag_city, container, false)
        if (activity is WeatherActivity) {
            val res = activity.resources
            val resId = res.getIdentifier("status_bar_height", "dimen", "android")
            val height = res.getDimensionPixelSize(resId)
            val padding = view.findViewById<LinearLayout>(R.id.padding_content)
            padding.setPadding(0, height, 0, 0)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list_view.setOnItemClickListener { _, _, position, _ ->
            when (currentLevel) {
                LEVEL_PROVINCE -> {
                    selectedProvince = provinceList[position]
                    queryCity()
                }
                LEVEL_CITY -> {
                    selectedCity = cityList[position]
                    queryCounty()
                }
                LEVEL_COUNTY -> {
                    selectedCounty = countyList[position]
                    defaultSharedPreferences.edit().putString("weather_id", selectedCounty!!.weather_id).apply()
                    if (activity is MainActivity) {
                        startActivity<WeatherActivity>()
                        activity.finish()
                    } else if (activity is WeatherActivity) {
                        val act = activity as WeatherActivity
                        act.drawer_layout.closeDrawers()
                        act.swipe_refresh.isRefreshing = true
                        act.requestWeather(selectedCounty!!.weather_id)
                    }
                }
            }
        }
        back_button!!.setOnClickListener {
            if (currentLevel == LEVEL_CITY)
                queryProvince()
            else if (currentLevel == LEVEL_COUNTY)
                queryCity()
        }
        queryProvince()
    }

    private fun queryProvince() {
        title_text.text = "中国"
        back_button.visibility = View.INVISIBLE
        showProgress()

        doAsync {
            val s = URL(Constant.CITY_URL).readText()
            uiThread {
                closeProgress()
                val t = object : TypeToken<List<Province>>() {}.type
                provinceList = Gson().fromJson<List<Province>>(s, t) as ArrayList<Province>
                val adapter = ProvinceAdapter(context, R.layout.item_city, provinceList)
                list_view.adapter = adapter
                list_view.setSelection(0)
                currentLevel = LEVEL_PROVINCE
            }
        }
    }

    private fun queryCity() {
        title_text.text = selectedProvince!!.name
        back_button!!.visibility = View.VISIBLE
        showProgress()

        doAsync {
            val s = URL(Constant.CITY_URL + "/" + selectedProvince!!.id).readText()

            uiThread {
                closeProgress()
                val t = object : TypeToken<List<Citys>>() {}.type
                cityList = Gson().fromJson<List<Citys>>(s, t) as ArrayList<Citys>
                val adapter = CityAdapter(context, R.layout.item_city, cityList)
                list_view.adapter = adapter
                list_view.setSelection(0)
                currentLevel = LEVEL_CITY
            }
        }
    }

    private fun queryCounty() {
        title_text.text = selectedCity!!.name
        back_button.visibility = View.VISIBLE
        showProgress()

        doAsync {
            val s = URL(Constant.CITY_URL + "/" + selectedProvince!!.id + "/" + selectedCity!!.id).readText()
            uiThread {
                closeProgress()
                val t = object : TypeToken<List<County>>() {}.type
                countyList = Gson().fromJson<List<County>>(s, t) as ArrayList<County>
                val adapter = CountyAdapter(context, R.layout.item_city, countyList)
                list_view.adapter = adapter
                list_view.setSelection(0)
                currentLevel = LEVEL_COUNTY
            }
        }
    }

    private var progress: ProgressDialog? = null

    private fun showProgress(message: String = "加载中") {
        if (progress == null) {
            progress = ProgressDialog(activity)
            progress!!.setMessage(message)
            progress!!.setCancelable(false)
        }
        progress!!.show()
    }

    private fun closeProgress() {
        if (progress != null)
            progress!!.dismiss()
    }

    companion object {
        @JvmStatic
        private val LEVEL_PROVINCE = 0
        @JvmStatic
        private val LEVEL_CITY = 1
        @JvmStatic
        private val LEVEL_COUNTY = 2
    }

}