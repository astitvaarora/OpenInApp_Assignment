package com.example.openinapp.Network

import com.example.openinapp.Models.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers


private const val token : String = "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
interface ApiService {
    @GET("api/v1/dashboardNew")
    @Headers(token)
    fun fetchData(): Call<ApiResponse>
}

