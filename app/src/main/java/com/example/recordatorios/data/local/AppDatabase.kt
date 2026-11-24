package com.example.recordatorios.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Base de datos principal de la aplicacion construida con Room
@Database(
    entities = [RecordatorioEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertidores::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordatorioDao(): RecordatorioDao
}

// Fabrica singleton para crear y reutilizar la base de datos
fun crearBase(context: Context): AppDatabase =
    Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "recordatorios.db"
    ).build()
