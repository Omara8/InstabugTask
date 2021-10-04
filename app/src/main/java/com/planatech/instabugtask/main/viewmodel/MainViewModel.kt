package com.planatech.instabugtask.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.planatech.instabugtask.main.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val mainRepository: MainRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private var _frequency = MutableLiveData<MutableMap<String, Int>>()
    val frequency: LiveData<MutableMap<String, Int>> = _frequency

    fun loadWebsite(url: String) {
        _isLoading.postValue(true)
        GlobalScope.launch {
            val result = mainRepository.loadWebsite(url)
            handleResult(result)
        }
    }

    private fun handleResult(result: String) {
        var words: MutableList<String> = mutableListOf()
        val regex = "[^A-Za-z0-9 ]".toRegex()
        val array = regex.replace(result, "").split(" ")
        array.forEachIndexed { index, s ->
            if (s.length > 1)
                words.add(s)
        }
        findOccurences(words)
    }

    private fun findOccurences(words: MutableList<String>) {
        val frequencyMap: MutableMap<String, Int> = HashMap()
        words.forEach {
            var count = frequencyMap[it]
            if (count == null) count = 0
            frequencyMap[it] = count + 1
        }
        frequencyMap.remove("")
        _isLoading.postValue(false)
        _frequency.postValue(frequencyMap)
    }

}