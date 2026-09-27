package ru.chivarzin.aleksandr.playlistmaker.domain.api

interface FileRepository {
    fun saveFile(sourceUri: String, filename_without_extension: String): String
}