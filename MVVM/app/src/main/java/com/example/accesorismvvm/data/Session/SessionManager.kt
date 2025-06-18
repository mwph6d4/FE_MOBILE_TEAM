package com.example.accesorismvvm.data.Session

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveSession(userId: Int) {
        prefs.edit().putInt("user_id", userId).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt("user_id", -1)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun saveToken(token: String?) {
        prefs.edit().putString("auth_token", token).apply()
    }

}
