package com.example.uaspemrogrammanmobile.data.repository

import com.example.uaspemrogrammanmobile.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Cek sesi pengguna yang sedang login
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    // Fungsi Login
    suspend fun login(email: String, pass: String): Resource<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            val user = result.user
            if (user != null) Resource.Success(user)
            else Resource.Error("Gagal memuat data pengguna.")
        } catch (e: Exception) {
            Resource.Error(mapFirebaseError(e))
        }
    }

    // Fungsi Registrasi + Simpan Nama Lengkap
    suspend fun register(name: String, email: String, pass: String): Resource<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val user = result.user
            if (user != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user.updateProfile(profileUpdates).await()
                Resource.Success(user)
            } else {
                Resource.Error("Registrasi gagal, silakan coba lagi.")
            }
        } catch (e: Exception) {
            Resource.Error(mapFirebaseError(e))
        }
    }

    // Fungsi Logout
    fun logout() {
        auth.signOut()
    }

    // Menerjemahkan kode error Firebase menjadi pesan jelas berbahasa Indonesia
    private fun mapFirebaseError(e: Exception): String {
        if (e is FirebaseAuthException) {
            return when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Format email tidak valid."
                "ERROR_WRONG_PASSWORD", "ERROR_INVALID_CREDENTIAL" -> "Email atau kata sandi salah."
                "ERROR_USER_NOT_FOUND" -> "Akun belum terdaftar. Silakan daftar terlebih dahulu."
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email ini sudah terdaftar. Gunakan email lain."
                "ERROR_WEAK_PASSWORD" -> "Kata sandi terlalu lemah (minimal 6 karakter)."
                "ERROR_NETWORK_REQUEST_FAILED" -> "Koneksi internet bermasalah. Periksa jaringan Anda."
                else -> "Terjadi kesalahan autentikasi: ${e.localizedMessage}"
            }
        }
        return e.localizedMessage ?: "Terjadi kesalahan jaringan."
    }
}