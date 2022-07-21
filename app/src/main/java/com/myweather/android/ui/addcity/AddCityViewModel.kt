package com.myweather.android.ui.addcity

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myweather.android.data.County
import com.myweather.android.data.HistoryCity
import com.myweather.android.data.HistoryWeatherInfo
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.base.Unit
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.qweather.sdk.view.HeConfig
import com.qweather.sdk.view.QWeather
import com.qweather.sdk.view.QWeather.OnResultWeatherDailyListener
import com.qweather.sdk.view.QWeather.OnResultWeatherNowListener
import org.litepal.LitePal
import org.litepal.extension.findAll
import kotlin.math.log

class AddCityViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "this is addCityActivity"
    }
    private var weatherId = MutableLiveData<String?>()
    private var historyWeatherInfoList = MutableLiveData<MutableList<HistoryWeatherInfo>>()

    val text: LiveData<String> = _text
    val getWeatherId: (String) -> LiveData<String?> = {
        queryWeatherId(it)
        weatherId
    }

    fun requestWeatherList(
        context: Context,
        historyCityList: List<HistoryCity>
    ): LiveData<MutableList<HistoryWeatherInfo>> {
        historyWeatherInfoList.value = arrayListOf()
        historyCityList?.forEach {
            requestWeather(context, it)
        }
        return historyWeatherInfoList
    }

    //查询历史记录城市列表
    fun queryHistoryCity(): MutableList<HistoryCity> {
        return LitePal.findAll<HistoryCity>()
    }

    //保存查询城市到本地
    fun addHistoryCity(cityName: String, weatherId: String) {
        val queryResultList: List<HistoryCity> =
            LitePal.where("cityName = ?", "$cityName").find(HistoryCity::class.java)
        if (queryResultList.isEmpty()) {
            var historyCity = HistoryCity()
            historyCity.cityName = cityName
            historyCity.weatherId = weatherId
            historyCity.save()
        }
    }

    //查询weatherId
    private fun queryWeatherId(cityName: String) {
        val queryResultList: List<County> =
            LitePal.where("countyName = ?", "$cityName").find(County::class.java)
        if (queryResultList.isNotEmpty())
            weatherId.value = queryResultList[0].weatherId.toString()
        else
            weatherId.value = null
    }

    //根据天气的id请求城市天气信息
    private fun requestWeather(context: Context, historyCity: HistoryCity) {
        //获取当天天气详情
        HeConfig.init("HE2205151337161848", "82de6db006c849da90967f4537daeb57")
        HeConfig.switchToDevService()
        QWeather.getWeatherNow(
            context,
            historyCity.weatherId,
            Lang.ZH_HANS,
            Unit.METRIC,
            object : OnResultWeatherNowListener {
                override fun onError(e: Throwable) {
                    Toast.makeText(context, "获取天气信息失败！", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(weatherNowBean: WeatherNowBean) {
                    //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                    if (Code.OK == weatherNowBean.code) {
                        //获取3天天气详情
                        QWeather.getWeather3D(
                            context,
                            historyCity.weatherId,
                            Lang.ZH_HANS,
                            Unit.METRIC,
                            object : OnResultWeatherDailyListener {
                                override fun onError(throwable: Throwable) {
                                    Toast.makeText(context, "获取失败！", Toast.LENGTH_SHORT).show()
                                }

                                override fun onSuccess(weatherDailyBean: WeatherDailyBean){
                                    var historyWeatherInfo = HistoryWeatherInfo(
                                        historyCity.cityName,
                                        weatherNowBean.now.temp + "°",
                                        weatherDailyBean.daily[0].tempMax + "°",
                                        weatherDailyBean.daily[0].tempMin + "°"
                                    )
                                    Log.d("asdf", "${historyWeatherInfo.cityName}")
                                    historyWeatherInfoList.value?.add(historyWeatherInfo)
                                    historyWeatherInfoList.postValue(historyWeatherInfoList.value)
                                    Log.d("asdf", "${historyWeatherInfoList.value?.size}")
                                }
                            }
                        )
                    } else {
                        //在此查看返回数据失败的原因
                        val code = weatherNowBean.code
                    }
                }
            }
        )
    }
}