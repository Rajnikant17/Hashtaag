package com.example.hashtag.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hashtag.repository.BusinessLogic
import com.example.hashtag.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ActivityViewModel
@ViewModelInject
constructor(application: Application, private val apiCallBusinessLogic: BusinessLogic) :
        AndroidViewModel(application) {
    val applicationGlobal = application
    var apiResponseLiveData: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    fun getImagePath(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetApiResponse -> {
                    apiCallBusinessLogic.execute(applicationGlobal).onEach { dataState ->
                        apiResponseLiveData.value = dataState
                    }.launchIn(viewModelScope)
                }
            }
        }
    }


    sealed class MainStateEvent {
        object GetApiResponse : MainStateEvent()
    }
}