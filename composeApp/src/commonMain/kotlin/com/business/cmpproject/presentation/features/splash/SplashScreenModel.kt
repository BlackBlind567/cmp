package com.business.cmpproject.presentation.features.splash

import cafe.adriel.voyager.core.model.ScreenModel
import com.business.cmpproject.core.storage.LocalStorage

class SplashScreenModel(
    private val storage: LocalStorage
) : ScreenModel {

    fun isLoggedIn(): Boolean {
        return storage.getToken() != null
    }
}