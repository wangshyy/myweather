package com.myweather.android.data

import org.litepal.crud.LitePalSupport

class HistoryCity(
    var cityName: String = "",
    var weatherId: String = ""
) : LitePalSupport()