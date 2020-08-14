package com.example.contentdownloader

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.contentdownloader.ui.splash.SplashViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for the implementation of [SplashViewModel]
 */
@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val viewModel = SplashViewModel()

    @Test
    fun `should navigate to next screen when splash animation ends`() {
        viewModel.onSplashAnimationEnd()

        Assert.assertEquals(true, viewModel.navigateToNextScreen.value)
    }
}