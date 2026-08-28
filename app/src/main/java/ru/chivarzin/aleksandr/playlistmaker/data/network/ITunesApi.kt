package ru.chivarzin.aleksandr.playlistmaker.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.chivarzin.aleksandr.playlistmaker.data.dto.SearchResult

interface ITunesApi  {
    @GET("/search?entity=song")
    suspend fun findMusic(@Query("term") text: String): SearchResult
}