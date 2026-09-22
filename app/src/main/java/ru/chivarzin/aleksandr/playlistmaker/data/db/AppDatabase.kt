package ru.chivarzin.aleksandr.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.chivarzin.aleksandr.playlistmaker.data.db.dao.PlaylistDao
import ru.chivarzin.aleksandr.playlistmaker.data.db.dao.TrackDao
import ru.chivarzin.aleksandr.playlistmaker.data.db.entity.PlaylistEntity
import ru.chivarzin.aleksandr.playlistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao

    }