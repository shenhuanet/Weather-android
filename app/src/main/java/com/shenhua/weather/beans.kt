package com.shenhua.weather

/**
 * Created by shenhua on 2018-03-22-0022.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
data class City(var aqi: String, var pm10: String, var pm25: String, var qlty: String)
data class Aqi(var city: City)
data class Update(var loc: String, var utc: String)
data class Basic(var city: String, var cnty: String, var id: String, var lat: String, var lon: String, var update: Update)
data class Now(var cond: Cond, var fl: String, var hum: String, var pcpn: String, var pres: String, var tmp: String, var vis: String, var wind_dir: String, var wind_sc: String)
data class Air(var brf: String, var txt: String)
data class Comf(var brf: String, var txt: String)
data class Cw(var brf: String, var txt: String)
data class Drsg(var brf: String, var txt: String)
data class Flu(var brf: String, var txt: String)
data class Sport(var brf: String, var txt: String)
data class Trav(var brf: String, var txt: String)
data class Uv(var brf: String, var txt: String)
data class Suggestion(var air: Air, var comf: Comf, var cw: Cw, var drsg: Drsg, var flu: Flu, var sport: Sport, var trav: Trav, var uv: Uv)
data class Astro(var mr: String, var ms: String, var sr: String, var ss: String)
data class Tmp(var max: String, var min: String)
data class Daily_forecast(var astro: Astro, var cond: DailyCond, var date: String, var hum: String, var pcpn: String, var pop: String, var pres: String, var tmp: Tmp, var uv: String, var vis: String, var wind: Wind)
data class Cond(var code: String, var txt: String)
data class DailyCond(var code_d: String, var code_n: String, var txt_d: String, var txt_n: String)
data class Wind(var deg: String, var dir: String, var sc: String, var spd: String)
data class Hourly_forecast(var cond: Cond, var date: String, var hum: String, var pop: String, var pres: String, var tmp: String, var wind: Wind)
data class HeWeather(var aqi: Aqi, var basic: Basic, var daily_forecast: List<Daily_forecast>, var hourly_forecast: List<Hourly_forecast>, var now: Now, var status: String, var suggestion: Suggestion)
data class Weather(var HeWeather: List<HeWeather>)