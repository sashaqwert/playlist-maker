package ru.chivarzin.aleksandr.playlistmaker.data

import ru.chivarzin.aleksandr.playlistmaker.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}