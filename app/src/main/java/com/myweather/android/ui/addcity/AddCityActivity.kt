package com.myweather.android.ui.addcity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myweather.android.adapter.RecyclerViewAdapter
import com.myweather.android.databinding.ActivityAddCityBinding
import com.myweather.android.data.HistoryCity
import com.myweather.android.data.HistoryWeatherInfo
import org.litepal.LitePal
import org.litepal.extension.findAll

class AddCityActivity : AppCompatActivity() {
    private var binding: ActivityAddCityBinding? = null
    private lateinit var addCityViewModel: AddCityViewModel
    private var historyCityList: MutableList<HistoryCity> = arrayListOf()
    private var historyWeatherInfoList: MutableList<HistoryWeatherInfo> = arrayListOf()

    private var searchEditText: EditText? = null
    private var searchText: String? = null
    private lateinit var searchCancelButton: Button

    private lateinit var cityCardRecyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
        addCityViewModel = ViewModelProvider(this).get(AddCityViewModel::class.java)
        binding = ActivityAddCityBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        init()
        initSearch()
        queryWeatherInfo()
        initAdapter()
    }

    private fun init() {
        searchCancelButton = binding!!.searchCancel
        //取消按钮监听
        searchCancelButton.setOnClickListener {
            searchEditText?.setText("")
            searchEditText?.clearFocus()
            hideKeyboard()
        }
        refreshHistoryCityList()
    }

    private fun initAdapter(){
        cityCardRecyclerView = binding!!.cityCardRv
        cityCardRecyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = RecyclerViewAdapter(historyWeatherInfoList)
        cityCardRecyclerView.adapter = recyclerViewAdapter
    }
    //软键盘搜索按钮监听
    private fun initSearch() {
        searchEditText = binding?.searchEditText
        searchEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchText = searchEditText?.text.toString()
                getResult()
            }
            false
        }
    }

    //获取搜索结果
    private fun getResult() {
        /*if (searchText != "") {
//            val weatherId: String? = addCityViewModel.querySearchWeatherId(searchText!!)
            var weatherId: String? = null
            addCityViewModel.getWeatherId(searchText!!).observe(this){
                weatherId = it
            }
            weatherId?.let {
                Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
                addCityViewModel.addHistoryCity(searchText!!,it)
            } ?: Toast.makeText(this, "请输入正确的城市名称！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "请先输入城市名称！", Toast.LENGTH_SHORT).show()
        }*/
        if (searchText != "") {
            addCityViewModel.getWeatherId(searchText!!).observe(this) {
                //it为返回的weatherId
                it?.let {
                    Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
                    addCityViewModel.addHistoryCity(searchText!!, it)
                    refreshHistoryCityList()
                } ?: Toast.makeText(this, "请输入正确的城市名称！", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "请先输入城市名称！", Toast.LENGTH_SHORT).show()
        }
    }
    //查询天气信息，并存入列表
    private fun queryWeatherInfo(){
        historyWeatherInfoList.clear()
        addCityViewModel.requestWeatherList(applicationContext,historyCityList).observe(this){
            historyWeatherInfoList.addAll(it)
        }
    }
    //刷新历史搜索城市列
    private fun refreshHistoryCityList(){
        historyCityList.clear()
        historyCityList.addAll(addCityViewModel.queryHistoryCity())
    }
    //隐藏软键盘
    private fun hideKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager =
                applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}