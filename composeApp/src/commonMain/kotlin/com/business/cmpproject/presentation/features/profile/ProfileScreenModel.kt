package com.business.cmpproject.presentation.features.profile

import com.business.cmpproject.core.base.BaseScreenModel
import com.business.cmpproject.core.storage.LocalStorage
import com.business.cmpproject.data.model.response.LoginResponse
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