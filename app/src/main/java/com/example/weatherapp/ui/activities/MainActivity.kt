package com.example.weatherapp.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adapter.RvAdapter
import com.example.weatherapp.constants.Constants
import com.example.weatherapp.data.remoteRepository.WeatherApiService
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.apiModel.threeHourly.ResponseListThreeHourly
import com.example.weatherapp.model.apiModel.threeHourly.ThreeHourlyModel
import com.example.weatherapp.model.roomModel.WeatherModelClass
import com.example.weatherapp.viewModel.WeatherViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var rvAdapter: RvAdapter
    private lateinit var viewModel: WeatherViewModel
    private lateinit var weatherList: ArrayList<ResponseListThreeHourly>
    private lateinit var weatherListRoom: ArrayList<WeatherModelClass>
    private lateinit var horizontalManager: RecyclerView.LayoutManager
    private lateinit var city: String
    private val apiKey = Constants.API_KEY
    val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val weatherApiService = retrofit.create(WeatherApiService::class.java)

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        val window = this.window
        window.statusBarColor = Color.parseColor("#122259")
        initialization()
        clickListener()
        isOnline(applicationContext)
        if (isOnline(this)) {
            Toast.makeText(applicationContext, getString(R.string.txt_connected), Toast.LENGTH_LONG)
                .show()
        } else {
            getDataFromRoomDB()
            Toast.makeText(
                applicationContext,
                getString(R.string.txt_not_connected),
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    private fun getDataFromRoomDB() {
        viewModel.allweatherData.observe(
            this,
            Observer { list ->

                if (list.get(0).temp >= 30) {
                    bindingMain.tvTemperatureValue.setTextColor(Color.RED)
                    bindingMain.tvTemperatureValue.text = list.get(0).temp.toString()
                } else {
                    bindingMain.tvTemperatureValue.setTextColor(Color.BLACK)
                    bindingMain.tvTemperatureValue.text = list.get(0).temp.toString()
                }
                bindingMain.locationValue.text = list.get(0).name
                val humidityValue = list.get(0).humidity.toString()
                bindingMain.tvHumidityValue.text = "$humidityValue%"
            },
        )
    }

    private fun clickListener() {
        bindingMain.imgSearch.setOnClickListener {
            if (bindingMain.searchViewEditText.text.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.txt_enter_text),
                    Toast.LENGTH_LONG,
                ).show()
            } else {
                bindingMain.progressBar.visibility = View.VISIBLE
                closeKeyboard()
                city = bindingMain.searchViewEditText.text.toString()
                callHourlyApi(city)
            }
        }
    }

    @SuppressLint("ServiceCast")
    private fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun initialization() {
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        weatherList = ArrayList()
        weatherListRoom = ArrayList()
    }

    fun kelvinToCelsius(kelvin: Int): Int {
        val celsius = kelvin - 273.15
        return celsius.roundToInt()
    }

    private fun callHourlyApi(city: String) {
        val location = "$city"
        val call = weatherApiService.getHourlyWeather(location, apiKey)
        call.enqueue(object : Callback<ThreeHourlyModel> {
            override fun onResponse(
                call: Call<ThreeHourlyModel>,
                response: Response<ThreeHourlyModel>,
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    val tempInKelvin: Int? = weatherResponse?.list?.get(0)?.main?.temp?.toInt()
                    val celsiusTemperature = tempInKelvin?.let { kelvinToCelsius(it) }

                    if (celsiusTemperature!! >= 30) {
                        bindingMain.tvTemperatureValue.setTextColor(Color.RED)
                        bindingMain.tvTemperatureValue.text = celsiusTemperature.toString()
                    } else {
                        bindingMain.tvTemperatureValue.setTextColor(Color.BLACK)
                        bindingMain.tvTemperatureValue.text = celsiusTemperature.toString()
                    }
                    val humidity = response.body()!!.list.get(0).main.humidity
                    val cityName = response.body()!!.city.name
                    val model = celsiusTemperature.let {
                        WeatherModelClass(
                            0,
                            it,
                            humidity,
                            cityName,
                        )
                    }
                    GlobalScope.launch {
                        model.let { viewModel.insertRecord(it) }
                    }
                    bindingMain.progressBar.visibility = View.GONE
                    bindingMain.locationValue.text = cityName
                    bindingMain.tvHumidityValue.text = "$humidity%"
                    val weatherIcon = response.body()!!.list.get(0).weather.get(0).main
                    if (weatherIcon.equals("Smoke")) {
                        bindingMain.imgWeatherIcon.setImageResource(R.drawable.smoke)
                    } else if (weatherIcon.equals("clear sky")) {
                        bindingMain.imgWeatherIcon.setImageResource(R.drawable.clear_sky)
                    } else if (weatherIcon.equals("haze")) {
                        bindingMain.imgWeatherIcon.setImageResource(R.drawable.haze)
                    } else if (weatherIcon.equals("sunny")) {
                        bindingMain.imgWeatherIcon.setImageResource(R.drawable.sunny) } else if (weatherIcon.equals("Clouds")) {
                        bindingMain.imgWeatherIcon.setImageResource(R.drawable.clouds_icon)
                    } else {
                        bindingMain.imgWeatherIcon.setImageResource(R.drawable.night)
                    }

                    rvAdapter = RvAdapter(response.body()!!.list as ArrayList<ResponseListThreeHourly>)
                    horizontalManager =
                        LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.HORIZONTAL,
                            false,
                        )
                    bindingMain.rvHorizontal.apply {
                        setHasFixedSize(true)
                        layoutManager = horizontalManager
                        adapter = rvAdapter
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.txt_error),
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }

            override fun onFailure(call: Call<ThreeHourlyModel>, t: Throwable) {
                Toast.makeText(applicationContext, getString(R.string.txt_error), Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
