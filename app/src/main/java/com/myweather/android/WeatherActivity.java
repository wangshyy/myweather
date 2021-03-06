package com.myweather.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.myweather.android.ui.addcity.AddCityActivity;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import org.w3c.dom.Text;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView feelsLikeText;
    private TextView windDirText;
    private TextView humidityText;
    private TextView windScaleText;
    private TextView precipText;
    private TextView visText;
    public DrawerLayout drawerLayout;
    private Button navButton;
    private Button switchPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        init();
        String weatherId = getIntent().getStringExtra("weather_id");
        String cityName = getIntent().getStringExtra("county_name");
        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(weatherId, cityName);
        navButton.setOnClickListener(view -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        switchPage.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddCityActivity.class);
            startActivity(intent);
        });
    }

    private void init() {
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        feelsLikeText = findViewById(R.id.feels_like_text);
        windDirText = findViewById(R.id.wind_dir_text);
        humidityText = findViewById(R.id.humidity_text);
        windScaleText = findViewById(R.id.wind_scale_text);
        precipText = findViewById(R.id.precip_text);
        visText = findViewById(R.id.vis_text);
        drawerLayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.nav_button);
        switchPage = findViewById(R.id.switch_page);
    }

    /**
     * ???????????????id????????????????????????
     */
    public void requestWeather(final String weatherId, String cityName) {
        HeConfig.init("HE2205151337161848", "82de6db006c849da90967f4537daeb57");
        HeConfig.switchToDevService();
        QWeather.getWeatherNow(WeatherActivity.this, weatherId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Toast.makeText(WeatherActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                //??????????????????status??????????????????status???????????????????????????status?????????????????????status?????????Code???????????????
                if (Code.OK == weatherBean.getCode()) {
                    showWeatherInfo(weatherBean, weatherId, cityName);

                } else {
                    //???????????????????????????????????????
                    Code code = weatherBean.getCode();
                }
            }
        });
    }

    /**
     * ????????????????????????
     */
    private void showWeatherInfo(WeatherNowBean weatherBean, String weatherId, String cityName) {
        String degree = weatherBean.getNow().getTemp() + "??";
        String weatherInfo = weatherBean.getNow().getText();
        String feelsLike = weatherBean.getNow().getFeelsLike() + "??";
        String windScale = weatherBean.getNow().getWindScale();
        String windDir = weatherBean.getNow().getWindDir();
        String humidity = weatherBean.getNow().getHumidity() + "%";
        String precip = weatherBean.getNow().getPrecip() + "mm";
        String vis = weatherBean.getNow().getVis() + "km";
        titleCity.setText(cityName);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        feelsLikeText.setText(feelsLike);
        windDirText.setText(windDir);
        humidityText.setText(humidity);
        windScaleText.setText(windScale);
        precipText.setText(precip);
        visText.setText(vis);
        forecastLayout.removeAllViews();

        /**
         * ??????3???????????????
         */
        QWeather.getWeather3D(WeatherActivity.this, weatherId, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(WeatherActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                for (WeatherDailyBean.DailyBean dailyBean : weatherDailyBean.getDaily()) {
                    View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item, forecastLayout, false);
                    TextView dataText = view.findViewById(R.id.data_text);
                    TextView infoText = view.findViewById(R.id.info_text);
                    TextView maxText = view.findViewById(R.id.max_text);
                    TextView minText = view.findViewById(R.id.min_text);
                    dataText.setText(dailyBean.getFxDate().substring(5, dailyBean.getFxDate().length()));
                    infoText.setText(dailyBean.getTextDay());
                    maxText.setText(dailyBean.getTempMax() + "??");
                    minText.setText(dailyBean.getTempMin() + "??");
                    forecastLayout.addView(view);
                }
            }
        });
        weatherLayout.setVisibility(View.VISIBLE);
    }


}