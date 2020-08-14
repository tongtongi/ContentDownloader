package com.example.contentdownloader.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.contentdownloader.utils.SingleLiveEvent

class SplashViewModel @ViewModelInject constructor() : ViewModel() {

    val navigateToNextScreen = SingleLiveEvent<Boolean>()

    fun onSplashAnimationEnd() {
        navigateToNextScreen.value = true
    }
}