package com.brightfuture.viewmodels

import android.Manifest
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightfuture.models.WordFlow
import com.brightfuture.models.WordSearching
import com.brightfuture.repository.Repository
import com.brightfuture.utils.Functions
import com.brightfuture.utils.Resource
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class DictionaryViewModel : ViewModel() {

    fun getWords(): LiveData<Resource<Pair<Boolean, Boolean>>> {
        val response = MutableLiveData<Resource<Pair<Boolean, Boolean>>>()
        response.postValue(Resource.loading(null))
        viewModelScope.launch {
            Repository.getWords().collect {
                response.postValue(it)
            }
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

    fun permissionOfRecordAudio(context: Context): LiveData<Int> {
        val response = MutableLiveData<Int>()
        Dexter.withContext(context).withPermission(Manifest.permission.RECORD_AUDIO)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(r: PermissionGrantedResponse?) {
                    response.value = 1
                }

                override fun onPermissionDenied(r: PermissionDeniedResponse?) {
                    if (r?.isPermanentlyDenied == true) {
                        response.value = -1
                    } else {
                        response.value = 0
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?,
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
        return response
    }


    private val _suggestions = MutableStateFlow<List<WordSearching>>(emptyList())
    val suggestions: StateFlow<List<WordSearching>> = _suggestions.asStateFlow()

    fun searchingWords(query: String) {
        viewModelScope.launch {
            Functions.db.wordDao().searchingWords(query)
                .debounce(100) // 300ms debounce
                .collectLatest {
                    val list = it.map { rawWord ->
                        WordSearching(id = rawWord.id, name = rawWord.name)
                    }
                    _suggestions.value = list
                }
        }
    }
}