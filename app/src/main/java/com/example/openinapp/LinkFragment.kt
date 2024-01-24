package com.example.openinapp

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openinapp.Models.ApiResponse
import com.example.openinapp.Models.global_starts_model
import com.example.openinapp.Models.link_model
import com.example.openinapp.Network.ApiService
import com.example.openinapp.databinding.FragmentLinkBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

const val BASE_URL = "https://api.inopenapp.com/"
class LinkFragment : Fragment() {
    private var _binding: FragmentLinkBinding? = null
    private val binding get() = _binding!!
    private var ls = listOf<global_starts_model>()
    private var ls_1 = listOf<link_model>()
    private var graph_data = mutableListOf<Coordinates>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLinkBinding.inflate(inflater,container,false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupData() {

        binding.greetingTV.text = getGreeting(LocalTime.now())
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiResponse = apiService.fetchData()
                // Access data from apiResponse as needed
                val response = apiResponse.execute()
                if(response.isSuccessful){
                    val data = response.body()
                    if (data != null) {
                        requireActivity().runOnUiThread{
                                ls = storeData_globalStats(data)
                                if (data.data.recentLinks.size > 0){
                                    graphVisualization(data)
                                }

                                binding.globalStatsRV.adapter = global_stats_adapter(ls)
                            val chip_recentlink = binding.recentlinksChip
                            val chip_toplink = binding.toplinkChip

                            chip_recentlink.setOnCheckedChangeListener { _, isChecked ->
                                // Change the background tint color based on the checked state
                                val colorResId = if (isChecked) R.color.selected_chip_color else R.color.default_chip_color
                                chip_recentlink.setChipBackgroundColorResource(colorResId)
                                // Change the text color based on the checked state
                                val textColorResId = if (isChecked) R.color.selected_chip_text_color else R.color.default_chip_text_color
                                chip_recentlink.setTextColor(ContextCompat.getColor(requireContext(), textColorResId))
                                if (isChecked) {
                                    // Populate the RecyclerView with recent links
                                    ls_1 = storeData_RecentLink(data)
                                    binding.linkDisplayRV.adapter = link_adapter(requireContext(), ls_1)
                                } else {
                                    // Optionally, you can clear the RecyclerView when the chip is not checked
                                    binding.linkDisplayRV.adapter = null
                                }
                            }


                            chip_toplink.setOnCheckedChangeListener { _, isChecked ->
                                // Change the background tint color based on the checked state
                                val colorResId = if (isChecked) R.color.selected_chip_color else R.color.default_chip_color
                                chip_toplink.setChipBackgroundColorResource(colorResId)

                                // Change the text color based on the checked state
                                val textColorResId = if (isChecked) R.color.selected_chip_text_color else R.color.default_chip_text_color
                                chip_toplink.setTextColor(ContextCompat.getColor(requireContext(), textColorResId))
                                if (isChecked) {
                                    // Populate the RecyclerView with recent links
                                    ls_1 = storeData_TopLink(data)
                                    binding.linkDisplayRV.adapter = link_adapter(requireContext(), ls_1)
                                } else {
                                    // Optionally, you can clear the RecyclerView when the chip is not checked
                                    binding.linkDisplayRV.adapter = null
                                }

//                                ls_1 = storeData_RecentLink(data)
//                                binding.linkDisplayRV.adapter = link_adapter(requireContext(),ls_1)
                            }

                        }
                    }
                }else{
                    println("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }



        binding.globalStatsRV.layoutManager =
            LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)

        binding.globalStatsRV.adapter = global_stats_adapter(ls)

        binding.linkDisplayRV.layoutManager =
            LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        binding.linkDisplayRV.adapter = link_adapter(requireContext(),ls_1)



    }
    private fun graphVisualization(Data:ApiResponse){
        val ls = graphData(Data)
        val entries = ArrayList<Entry>()
        ls.distinct().sortedBy { it.month }.forEach{
            entries.add(Entry(it.month.toFloat(),it.click.toFloat()))
        }

        val vl = LineDataSet(entries, "My Type")
        Log.d("efewfe","${vl}")

        vl.lineWidth = 3f
        vl.fillColor = Color.BLUE
        vl.fillAlpha = Color.CYAN
        binding.Graph.setTouchEnabled(true);


        binding.apply {
            Graph.xAxis.granularity = 1f
            Graph.data = LineData(vl)
            Graph.setNoDataText("mokemoe")
            Graph.invalidate()
        }



    }
    private fun graphData(Data:ApiResponse):List<Coordinates>{
        val recent = Data.data.recentLinks
        for(click in recent){
            val coor = Coordinates(
                click.totalClicks,
                getMonthValue(click.createdAt)
            )
            graph_data.add(coor)
        }
        Log.d("ef2","{$graph_data}")
        return graph_data
    }
    private fun storeData_RecentLink(Data: ApiResponse): List<link_model> {
        val ls = mutableListOf<link_model>()
        for (linkObj in Data.data.recentLinks) {
            val linkModel = link_model(
                linkObj.originalImage,
                linkObj.app,
                linkObj.createdAt,
                linkObj.smartLink,
                linkObj.totalClicks.toString()
            )
            ls.add(linkModel)
        }
        return ls
    }
    private fun storeData_TopLink(Data: ApiResponse): List<link_model> {
        val ls = mutableListOf<link_model>()
        for (linkObj in Data.data.topLinks) {
            val linkModel = link_model(
                linkObj.originalImage,
                linkObj.app,
                linkObj.createdAt,
                linkObj.smartLink,
                linkObj.totalClicks.toString()
            )
            ls.add(linkModel)
        }
        return ls
    }

    private fun storeData_globalStats(data:ApiResponse):List<global_starts_model>{
        val global_stats_ls = listOf<global_starts_model>(
            global_starts_model(
                R.drawable.today_click_icon,
                data.todayClicks.toString(),
                "Today's click"
            ),
            global_starts_model(
                R.drawable.location_icon,
                data.topLocation,
                "Top Location"
            ),
            global_starts_model(
                R.drawable.web_icon,
                data.topSource,
                "Top source"
            ),
            global_starts_model(
                R.drawable.time_icon,
                data.startTime,
                "Best Time"
            )
        )
        return global_stats_ls
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getGreeting(currentTime: LocalTime): String {
        return when {
            currentTime.hour < 12 -> "Good morning!"
            currentTime.hour < 18 -> "Good afternoon!"
            else -> "Good evening!"
        }
    }

    fun getMonthValue(dateString: String): Int {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val date = dateFormat.parse(dateString)

            // Check if the date is not null
            date?.let {
                val calendar = Calendar.getInstance()
                calendar.time = date
                return calendar.get(Calendar.MONTH) + 1 // Months are zero-based in Calendar, so add 1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Return -1 if parsing fails
        return -1
    }

    fun getShortMonthName(monthValue: Int): String {
        if (monthValue in 1..12) {
            // Format the month value to get the short name
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, monthValue - 1) // Months are zero-based in Calendar
            val monthFormat = SimpleDateFormat("MMM", Locale.US)
            return monthFormat.format(calendar.time)
        }

        // Return an empty string if the month value is invalid
        return ""
    }


}

data class Coordinates(
    var click : Int,
    val month : Int
)
