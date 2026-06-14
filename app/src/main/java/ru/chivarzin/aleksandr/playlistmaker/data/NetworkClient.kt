package ru.chivarzin.aleksandr.playlistmaker.data

import ru.chivarzin.aleksandr.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}