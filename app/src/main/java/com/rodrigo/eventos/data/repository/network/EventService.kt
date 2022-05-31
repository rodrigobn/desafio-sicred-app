package com.rodrigo.eventos.data.repository.network

import com.rodrigo.eventos.data.model.CheckIn
import com.rodrigo.eventos.data.model.Event
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EventService {

    @GET("/api/events")
    suspend fun listEvents(@Query("title") search: String?): List<Event>

    @POST("/api/checkin")
    suspend fun checkIn(@Body checkIn: CheckIn)
}