package com.example.accesorismvvm.ui.Profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesorismvvm.data.Session.SessionManager
import com.example.accesorismvvm.data.local.entityDAO.UserEntity
import com.example.accesorismvvm.domain.repository.AuthRepository
import com.example.accesorismvvm.domain.repository.UserRepository
import com.example.accesorismvvm.domain.model.User
import com.example.accesorismvvm.data.mapper.toDomain
import com.example.accesorismvvm.data.remote.response.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // --- State untuk UI Profil ---
    private val _userProfile = MutableStateFlow<User?>(null) // Gunakan domain model User
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    private val _usernameInput = MutableStateFlow("") // State untuk input username yang akan diedit
    val usernameInput: StateFlow<String> = _usernameInput.asStateFlow()

    private val _editMode = MutableStateFlow(false) // State untuk mengontrol mode edit UI
    val editMode: StateFlow<Boolean> = _editMode.asStateFlow()

    private val _isLoading = MutableStateFlow(false) // State untuk loading
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null) // Pesan status (sukses/error)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    // --- State untuk Event Logout ---
    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent.asStateFlow()

    init {
        // Observer untuk UserProfile dari Room (local database)
        // Ini akan update _userProfile dan _usernameInput secara real-time
        viewModelScope.launch {
            userRepository.getUserProfileStream().collect { user ->
                Log.d("ProfileVM", "userProfile stream update: $user")
                _userProfile.value = user
                user?.username?.let {
                    _usernameInput.value = it
                    Log.d("ProfileVM", "usernameInput di-set: $it")
                }
            }

        }


        // Fetch user profile dari server saat ViewModel pertama kali dibuat
        fetchUserProfile()
    }
    fun fetchUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _statusMessage.value = null

            try {
                val token = sessionManager.getToken()
                Log.d("ProfileVM", "Token didapat: $token")

                if (!token.isNullOrEmpty()) {
                    val syncedUser = userRepository.fetchUserProfile("Bearer $token")

                    if (syncedUser == null) {
                        _statusMessage.value = "Gagal memuat profil dari server."
                        Log.e("ProfileVM", "User null dari server.")
                    } else {
                        _userProfile.value = syncedUser
//                        _usernameInput.value = syncedUser.username

                        Log.d("ProfileVM", "User berhasil dimuat: ${syncedUser.email} - ${syncedUser.username}")
                    }
                } else {
                    _statusMessage.value = "Token tidak ditemukan. Silakan login ulang."
                    Log.e("ProfileVM", "Token null/empty.")
                }

            } catch (e: Exception) {
                Log.e("ProfileVM", "Gagal memuat profil: ${e.message}", e)
                _statusMessage.value = "Gagal memuat profil: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }





    // --- Fungsi untuk Mengubah Nilai Input Username ---
    fun onUsernameInputChange(newUsername: String) {
        _usernameInput.value = newUsername
    }

    // --- Fungsi untuk Mengubah Mode Edit ---
    fun toggleEditMode() {
        _editMode.value = !_editMode.value
        // Saat keluar dari edit mode, jika tidak ada perubahan disimpan, kembalikan input ke username asli
        if (!_editMode.value) {
            _userProfile.value?.username?.let {
                _usernameInput.value = it
            }
            _statusMessage.value = null // Hapus pesan status saat keluar mode edit
        }
    }

    // --- Fungsi untuk Menyimpan Username yang Diedit ---
    fun saveUsername() {
        viewModelScope.launch(Dispatchers.IO) { // Pastikan operasi jaringan di Dispatchers.IO
            _isLoading.value = true
            _statusMessage.value = null // Bersihkan pesan status sebelumnya

            try {
                val token = sessionManager.getToken()
                val currentUsername = _userProfile.value?.username
                val newUsername = _usernameInput.value.trim() // Hapus spasi di awal/akhir

                if (token.isNullOrEmpty()) {
                    _statusMessage.value = "Autentikasi diperlukan. Silakan login ulang."
                    return@launch
                }

                if (newUsername.isBlank()) {
                    _statusMessage.value = "Username tidak boleh kosong."
                    return@launch
                }

                if (newUsername == currentUsername) {
                    _statusMessage.value = "Username sama dengan yang sebelumnya."
                    _editMode.value = false // Otomatis keluar dari mode edit jika tidak ada perubahan
                    return@launch
                }

                // Panggil fungsi updateUsername dari UserRepository
                val updatedUser = userRepository.updateUsername("Bearer $token", newUsername) // Pastikan format tokennya "Bearer YOUR_TOKEN"

                if (updatedUser != null) {
                    _userProfile.value = updatedUser // Perbarui userProfile StateFlow dengan data terbaru
                    _statusMessage.value = "Username berhasil diperbarui!"
                    _editMode.value = false // Keluar dari mode edit setelah berhasil
                } else {
                    _statusMessage.value = "Gagal memperbarui username. Coba lagi."
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Gagal menyimpan username: ${e.message}", e)
                _statusMessage.value = "Error: ${e.localizedMessage ?: "Terjadi kesalahan."}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- Fungsi untuk Logout ---
    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
//            userRepository.clearUserProfile() // Hapus data user dari Room saat logout
            _logoutEvent.value = true
        }
    }

    fun clearLogoutEvent() {
        _logoutEvent.value = false
    }
}

//
//@HiltViewModel
//class ProfileViewModel @Inject constructor(
//    private val authRepository: AuthRepository,
//    private val userRepository: UserRepository,
//    private val sessionManager: SessionManager
//) : ViewModel() {
//
//    private val _user = mutableStateOf<UserEntity?>(null)
//    val user: State<UserEntity?> = _user
//
//    private val _logoutEvent = mutableStateOf(false)
//    val logoutEvent: State<Boolean> = _logoutEvent
//
//
//    init {
//        viewModelScope.launch {
//            val userId = sessionManager.getUserId()
//            val token = sessionManager.getToken()
//
//            try {
//                if (!token.isNullOrEmpty()) {
//                    authRepository.syncUserProfile(token)
//                }
//            } catch (e: Exception) {
//                Log.e("ProfileViewModel", "Gagal sync ke server: ${e.message}")
//            }
//
//            if (userId != -1) {
//                val userData = userRepository.getUserById(userId)
//                _user.value = userData
//            }
//        }
//    }
//
//    fun logout() {
//        sessionManager.clearSession()
//        _logoutEvent.value = true
//    }
//
//    fun clearLogoutEvent() {
//        _logoutEvent.value = false
//    }
//}
