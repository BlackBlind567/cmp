package com.hybrid.internet.presentation.features.profile

import com.hybrid.internet.core.base.BaseScreenModel
import com.hybrid.internet.core.storage.LocalStorage
import com.hybrid.internet.data.model.response.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileScreenModel(
    private val localStorage: LocalStorage,
    //private val repo: ProfileRepository // Api call ke liye
) : BaseScreenModel() {

    private val _userData = MutableStateFlow(localStorage.getUser())
    val userData: StateFlow<LoginResponse?> = _userData

    fun logout(onSuccess: () -> Unit) {
        screenModelScope.launch {
            // Loader dikhao
            //val result = repo.logoutApi() // Agar API hai logout ki
            localStorage.clear()
            onSuccess()
        }
    }
}