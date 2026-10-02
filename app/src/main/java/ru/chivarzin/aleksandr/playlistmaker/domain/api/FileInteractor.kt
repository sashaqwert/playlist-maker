package ru.chivarzin.aleksandr.playlistmaker.domain.api

interface FileInteractor {
    fun saveFile(sourceUri: String, filename_without_extension: String): String
}