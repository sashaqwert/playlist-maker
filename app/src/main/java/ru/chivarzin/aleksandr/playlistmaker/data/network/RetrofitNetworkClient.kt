package ru.chivarzin.aleksandr.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.chivarzin.aleksandr.playlistmaker.data.NetworkClient
import ru.chivarzin.aleksandr.playlistmaker.data.dto.Response
import ru.chivarzin.aleksandr.playlistmaker.data.dto.SearchRequest

class RetrofitNetworkClient(private val iTunesService: ITunesApi, private val context: Context) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            if (isConnected()) {
                try {
                    val resp = iTunesService.findMusic(dto.expression).execute()

                    val body = resp.body() ?: Response()

                    return body.apply { resultCode = resp.code() }
                } catch (_: Exception) {
                    return Response().apply { resultCode = -2 }
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