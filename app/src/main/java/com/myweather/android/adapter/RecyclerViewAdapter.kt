package com.myweather.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myweather.android.databinding.CityCardItemBinding

class RecyclerViewAdapter(): RecyclerView.Adapter<RecyclerViewAdapter.viewHolder>() {
    class viewHolder(binding: CityCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var degreeText = binding.degreeText
        var cityName = binding.cityName
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.viewHolder {
        var binding = CityCardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        var viewHolder = RecyclerViewAdapter.viewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.viewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}