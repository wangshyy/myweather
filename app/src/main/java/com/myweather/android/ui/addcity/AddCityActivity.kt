package com.myweather.android.ui.addcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myweather.android.R
import com.myweather.android.databinding.ActivityAddCityBinding
import com.myweather.android.db.CityCard

class AddCityActivity : AppCompatActivity() {
    private var binding: ActivityAddCityBinding? = null
    private lateinit var addCityViewModel: AddCityViewModel
    private var cityCardList: List<CityCard>? = null
    private var searchEditText: EditText? = null
    private var searchText: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
        addCityViewModel = ViewModelProvider(this).get(AddCityViewModel::class.java)
        binding = ActivityAddCityBinding.inflate(layoutInflater)

        init()
        search()
    }

    private fun init() {

    }

    private fun search() {
        /**
         *监听软键盘回车
        当点击回车按钮时获取输入框内容
         */
        searchEditText = binding?.searchEditText
        searchEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchText = searchEditText?.text.toString()
                Toast.makeText(this,searchText,Toast.LENGTH_SHORT).show()
                Log.d("122", searchText + "")
            }
            false
        }
    }
}