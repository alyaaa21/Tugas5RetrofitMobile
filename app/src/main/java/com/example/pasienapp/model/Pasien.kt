package com.example.pasienapp.model
data class Pasien(
    val id: Int? = null,
    val nama: String? = null,
    val tanggal_lahir: String? = null,
    val jenis_kelamin: String? = null,
    val alamat: String? = null,
    val no_telepon: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

data class PasienResponse(
    val success: Boolean,
    val message: String,
    val data: List<Pasien>?
)