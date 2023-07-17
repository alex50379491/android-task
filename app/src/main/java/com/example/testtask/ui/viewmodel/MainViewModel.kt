package com.example.testtask.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.testtask.data.ApiClient
import javax.inject.Inject

class MainViewModel @Inject constructor(private val apiClient: ApiClient, app: Application) :
    AndroidViewModel(app) {

    fun isUrlSet(): Boolean {
        return apiClient.isUrlSet()
    }
}