package ru.chivarzin.aleksandr.playlistmaker.domain.impl

import ru.chivarzin.aleksandr.playlistmaker.domain.api.FileInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.FileRepository

class FileInteractorImpl(private val repository: FileRepository): FileInteractor {
    override fun saveFile(
        sourceUri: String,
        filename_without_extension: String
    ): String {
        return repository.saveFile(sourceUri, filename_without_extension)
    }
}