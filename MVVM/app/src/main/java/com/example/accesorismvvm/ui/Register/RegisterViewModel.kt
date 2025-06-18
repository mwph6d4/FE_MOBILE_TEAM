package com.example.accesorismvvm.ui.Register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesorismvvm.data.local.entityDAO.UserEntity
import com.example.accesorismvvm.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*
import com.example.accesorismvvm.data.remote.request.RegisterRequest
import com.example.accesorismvvm.data.repositoryImpl.AuthRepositoryImpl
import com.example.accesorismvvm.domain.repository.AuthRepository

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var message by mutableStateOf("")

    fun register() {
        viewModelScope.launch {
            try {
                val result = authRepository.register(
                    name = username,
                    email = email,
                    password = password
                )

                message = if (result.success) {
                    "Registrasi berhasil!"
                } else {
                    result.message ?: "Registrasi gagal."
                }

            } catch (e: Exception) {
                message = e.message ?: "Terjadi kesalahan saat registrasi."
            }
        }
    }

}
