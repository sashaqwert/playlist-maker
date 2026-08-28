package ru.chivarzin.aleksandr.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.chivarzin.aleksandr.playlistmaker.data.NetworkClient
import ru.chivarzin.aleksandr.playlistmaker.data.dto.Response
import ru.chivarzin.aleksandr.playlistmaker.data.dto.SearchRequest

class RetrofitNetworkClient(private val iTunesService: ITunesApi, private val context: Context) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            if (isConnected()) {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = iTunesService.findMusic(dto.expression)
                        response.apply { resultCode = 200 }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }
            } else {
                return Response().apply { resultCode = -1 }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}