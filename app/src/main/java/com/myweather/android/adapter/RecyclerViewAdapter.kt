package com.myweather.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myweather.android.databinding.CityCardItemBinding
import com.myweather.android.data.HistoryCity
import com.myweather.android.data.HistoryWeatherInfo

class RecyclerViewAdapter(historyWeatherInfoList: MutableList<HistoryWeatherInfo>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var historyWeatherInfoList: MutableList<HistoryWeatherInfo> = historyWeatherInfoList

    class ViewHolder(binding: CityCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var degreeText = binding.degreeText
        var cityName = binding.cityName
        var minText = binding.minText
        var maxText = binding.maxText
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var binding =
            CityCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = historyWeatherInfoList[position].cityName
        holder.degreeText.text = historyWeatherInfoList[position].degree
        holder.maxText.text = historyWeatherInfoList[position].max
        holder.minText.text = historyWeatherInfoList[position].min
    }

    override fun getItemCount(): Int {
        return historyWeatherInfoList.size
    }
}