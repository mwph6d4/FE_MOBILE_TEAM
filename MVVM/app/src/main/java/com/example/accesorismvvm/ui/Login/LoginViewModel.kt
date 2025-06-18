package com.example.accesorismvvm.ui.Login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesorismvvm.data.Session.SessionManager
import com.example.accesorismvvm.data.local.entityDAO.UserEntity
import com.example.accesorismvvm.data.remote.request.LoginRequest
import com.example.accesorismvvm.data.repositoryImpl.AuthRepositoryImpl
import com.example.accesorismvvm.domain.repository.AuthRepository
import com.example.accesorismvvm.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var loginSuccess by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)

    fun loginUser() {
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)

                if (!response.auth_token.isNullOrEmpty() && response.user != null) {
                    val userDto = response.user

                    val userEntity = UserEntity(
                        id = userDto.id,
                        username = userDto.name,
                        email = userDto.email,
                        password = password,
                        role = response.user_role ?: "pembeli" // ✅ Ambil dari response langsung
                    )


                    // Simpan ke Room (override data lama)
                    userRepository.clearUserProfile()
                    userRepository.register(userEntity)

                    sessionManager.saveSession(userDto.id)
                    sessionManager.saveToken(response.auth_token)

                    loginSuccess = true
                    message = if (response.auth_token == "offline") {
                        "Login offline berhasil"
                    } else {
                        "Login berhasil"
                    }

                } else {
                    message = "Login gagal: ${response.message}"
                }

            } catch (e: Exception) {
                message = "Terjadi kesalahan: ${e.message}"
            }
        }
    }
}
