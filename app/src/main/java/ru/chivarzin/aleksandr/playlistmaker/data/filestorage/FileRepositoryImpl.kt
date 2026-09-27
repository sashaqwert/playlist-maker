package ru.chivarzin.aleksandr.playlistmaker.data.filestorage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import ru.chivarzin.aleksandr.playlistmaker.domain.api.FileRepository
import java.io.File
import java.io.FileOutputStream

class FileRepositoryImpl (private val context: Context): FileRepository {
    override fun saveFile(
        sourceUri: String,
        filename_without_extension: String
    ): String {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "${filename_without_extension}.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = context.contentResolver?.openInputStream(sourceUri.toUri())
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri().toString()
    }
}