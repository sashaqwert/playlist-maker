package ru.chivarzin.aleksandr.playlistmaker.presentation.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.chivarzin.aleksandr.playlistmaker.App
import ru.chivarzin.aleksandr.playlistmaker.creator.Creator

class SettingsViewModel (private val context : Context) : ViewModel() {
    companion object {

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SettingsViewModel(app)
            }
        }
    }

    private val themeInteractor = Creator.provideThemeInteractor(context)

    private val isDarkthemeLiveData = MutableLiveData<Boolean>(themeInteractor.getTheme())
    fun observeIsDarkTheme(): LiveData<Boolean> = isDarkthemeLiveData

    fun setThame(isDark: Boolean) {
        themeInteractor.saveTheme(isDark)
        (context as App).switchTheme(isDark)
        isDarkthemeLiveData.value = isDark
    }
}