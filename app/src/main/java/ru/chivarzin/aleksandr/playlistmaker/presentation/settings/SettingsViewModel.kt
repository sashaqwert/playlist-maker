package ru.chivarzin.aleksandr.playlistmaker.presentation.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.chivarzin.aleksandr.playlistmaker.App
import ru.chivarzin.aleksandr.playlistmaker.domain.api.ThemeInteractor

class SettingsViewModel (private val themeInteractor: ThemeInteractor, val context : Context) : ViewModel() {

    private val isDarkthemeLiveData = MutableLiveData<Boolean>(themeInteractor.getTheme())
    fun observeIsDarkTheme(): LiveData<Boolean> = isDarkthemeLiveData

    fun setThame(isDark: Boolean) {
        themeInteractor.saveTheme(isDark)
        (context as App).switchTheme(isDark)
        isDarkthemeLiveData.value = isDark
    }
}