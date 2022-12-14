package com.example.hangman.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hangman.api.WordNikService
import com.example.hangman.api.WordNikWOTDService
import com.example.hangman.data.LoadingStatus
import com.example.hangman.data.WordOfTheDayRepository
import com.example.hangman.data.WordRepository
import kotlinx.coroutines.launch

class WordViewModel : ViewModel() {
    private val repositoryWOTD = WordOfTheDayRepository(WordNikWOTDService.create())
    private val repository = WordRepository(WordNikService.create())

    private val _wordResult = MutableLiveData<String?>(null)
    val wordResult: LiveData<String?> = _wordResult

    private val _wordOfTheDayResult = MutableLiveData<String?>(null)
    val wordOfTheDayResult: LiveData<String?> = _wordOfTheDayResult

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadReqResult(
        maxLength: Int,
        key: String
    ) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadWordRequest(maxLength, key)
            _wordResult.value = result.getOrNull()
            _loadingStatus.value = when (result.isSuccess) {
                true ->  LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }

    fun loadWOTDResult(
        key: String
    ) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repositoryWOTD.loadWordOfTheDayRequest(key)
            _wordOfTheDayResult.value = result.getOrNull()
            _loadingStatus.value = when (result.isSuccess) {
                true ->  LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}