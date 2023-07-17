package com.example.testtask.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testtask.data.ApiClient
import com.example.testtask.util.Result
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val apiClient: ApiClient,
                                            app: Application) :
    AndroidViewModel(app) {

    val isUrlValidData = MutableSharedFlow<Boolean>(replay = 0)
    val testConnectionResultData = MutableSharedFlow<Result<Boolean>>(replay = 0)
    val urlData = MutableLiveData<String?>(apiClient.getUrl())
    private val compositeDisposable = CompositeDisposable()

    init {
        emitIsUrlValid(apiClient.getUrl()?.isNotBlank() ?: false)
    }

    fun testConnection(url: String) {
        compositeDisposable.add(
            apiClient.testUrl(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { emitConnectionTestResult(Result.Loading()) }
                .subscribe(
                    {
                        emitConnectionTestResult(Result.Success(true))
                        emitIsUrlValid(true)
                    },
                    {
                        emitIsUrlValid(false)
                        emitConnectionTestResult(Result.Failure(it))
                    })
        )
    }

    fun saveUrl(url: String) {
        compositeDisposable.add(
            apiClient.setUrl(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { emitConnectionTestResult(Result.Loading()) }
                .subscribe(
                    {
                        emitConnectionTestResult(Result.Success(true))
                        urlData.value = url
                    },
                    { emitConnectionTestResult(Result.Failure(it)) })
        )
    }

    private fun emitIsUrlValid(isValid: Boolean) {
        viewModelScope.launch {
            isUrlValidData.emit(isValid)
        }
    }

    private fun emitConnectionTestResult(result: Result<Boolean>) {
        viewModelScope.launch {
            testConnectionResultData.emit(result)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun updateUrl(url: String?) {
        if (!urlData.value.equals(url))
            urlData.value = url
    }
}