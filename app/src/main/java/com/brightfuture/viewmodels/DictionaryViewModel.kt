package com.brightfuture.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightfuture.repository.Repository
import com.brightfuture.utils.Resource
import kotlinx.coroutines.launch

class DictionaryViewModel : ViewModel() {

    fun getWords(): LiveData<Resource<Pair<Boolean, Boolean>>> {
        val response = MutableLiveData<Resource<Pair<Boolean, Boolean>>>()
        viewModelScope.launch {
            response.postValue(Repository.getWords())
        }
        return response
    }

    fun getWord(): LiveData<Resource<Boolean>> {
        val response = MutableLiveData<Resource<Boolean>>()
        viewModelScope.launch {
            response.postValue(Repository.getWord())
        }
        return response
    }
}