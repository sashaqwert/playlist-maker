package ru.chivarzin.aleksandr.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi  {
    @GET("/search?entity=song")
    fun findMusic(@Query("term") text: String): Call<SearchResult>
}