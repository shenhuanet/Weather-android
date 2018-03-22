package com.shenhua.weather

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.jetbrains.anko.find

/**
 * Created by shenhua on 2018-03-22-0022.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
data class Province(val id: Int, val name: String)

data class Citys(val id: Int, val name: String)
data class County(val id: Int, val name: String, val weather_id: String)

class ProvinceAdapter(context: Context?, resource: Int, objects: List<Province>) : ArrayAdapter<Province>(context, resource, objects) {

    private val resId: Int = resource

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item: Province = getItem(position)
        val view = LayoutInflater.from(context).inflate(resId, parent, false)
        val itemName = view.find<TextView>(R.id.item_name)
        itemName.text = item.name
        return view
    }
}

class CityAdapter(context: Context?, resource: Int, objects: List<Citys>) : ArrayAdapter<Citys>(context, resource, objects) {

    private val resId: Int = resource

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item: Citys = getItem(position)
        val view = LayoutInflater.from(context).inflate(resId, parent, false)
        val itemName = view.find<TextView>(R.id.item_name)
        itemName.text = item.name
        return view
    }
}

class CountyAdapter(context: Context?, resource: Int, objects: List<County>) : ArrayAdapter<County>(context, resource, objects) {

    private val resId: Int = resource

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item: County = getItem(position)
        val view = LayoutInflater.from(context).inflate(resId, parent, false)
        val itemName = view.find<TextView>(R.id.item_name)
        itemName.text = item.name
        return view
    }
}

