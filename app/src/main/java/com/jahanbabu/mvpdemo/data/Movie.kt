package com.jahanbabu.mvpdemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class Movie(
    @PrimaryKey
    var id: String,
    var title: String,
    var description: String,
    var thumb: String,
    var url: String,
    var position: Long = 0
)