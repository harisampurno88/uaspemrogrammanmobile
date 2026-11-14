package com.example.uaspemrogrammanmobile.data.model

import java.io.Serializable

data class Plant(
    val id: String = "",
    val namaSpesies: String = "",
    val kategori: String = "",
    val kondisi: String = "",
    val deskripsi: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String = "",
    val uploaderId: String = "",
    val uploaderName: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Serializable