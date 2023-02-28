package com.example.weatherapp.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.apiModel.threeHourly.ResponseListThreeHourly
import kotlin.math.roundToInt

class RvAdapter(
    var weatherList: ArrayList<ResponseListThreeHourly>,
) : RecyclerView.Adapter<RvAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.single_layout_rv,
            parent,
            false,
        )

        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = weatherList[position]
        holder.time.text = post.dt_txt

        val HourlyTempCelcius = kelvinToCelsius(post.main.temp.toInt())
        holder.temperature.text = HourlyTempCelcius.toString()
        val iconImageText = post.weather.get(0).main
        if (iconImageText.equals("Smoke")) {
            holder.icon.setImageResource(R.drawable.smoke)
        } else if (iconImageText.equals("Clear")) {
            holder.icon.setImageResource(R.drawable.clear_sky)
        } else if (iconImageText.equals("haze")) {
            holder.icon.setImageResource(R.drawable.haze)
        } else if (iconImageText.equals("sunny")) {
            holder.icon.setImageResource(R.drawable.sunny)
        } else if (iconImageText.equals("Clouds")) {
            holder.icon.setImageResource(R.drawable.clouds_icon)
        } else {
            holder.icon.setImageResource(R.drawable.night)
        }

        // convertDateTime(post.dt_txt)
    }

    /*private fun convertDateTime(dtTxt: String) {
        val dateTime = dtTxt.toInt()
        Log.d("TimeDate", dateTime.toString())
    }*/

    fun kelvinToCelsius(kelvin: Int): Int {
        val celsius = kelvin - 273.15
        return celsius.roundToInt()
    }
    override fun getItemCount(): Int {
        return weatherList.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temperature: TextView = itemView.findViewById(R.id.tvTemperatureValueRv)
        val time: TextView = itemView.findViewById(R.id.tvTimeRv)
        val icon: ImageView = itemView.findViewById(R.id.imgIconRv)
    }
}
