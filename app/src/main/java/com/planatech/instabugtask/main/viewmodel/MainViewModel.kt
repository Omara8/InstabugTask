package com.planatech.instabugtask.main.viewmodel

import androidx.core.text.isDigitsOnly
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

    private var _results = MutableLiveData<MutableMap<String, Int>>()
    val results: LiveData<MutableMap<String, Int>> = _results

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
        array.forEachIndexed { _, s ->
            if (s.isNotEmpty() && !s.isDigitsOnly())
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
        _results.postValue(frequencyMap)
        _frequency.postValue(frequencyMap)
    }

    fun sortList(isDescending: Boolean) {
        when (isDescending) {
            true -> {
                val sortedMap =
                    frequency.value?.toList()?.sortedByDescending { (_, value) -> value }?.toMap()
                        ?.toMutableMap()
                _frequency.postValue(sortedMap)
            }
            false -> {
                val sortedMap =
                    frequency.value?.toList()?.sortedBy { (_, value) -> value }?.toMap()
                        ?.toMutableMap()
                _frequency.postValue(sortedMap)
            }
        }
    }

    fun search(query: String?) {
        val tempMap: MutableMap<String, Int> = HashMap()
        frequency.value?.filterKeys { it.contains(query!!) }?.onEach {
            tempMap[it.key] = it.value
        }
        _frequency.postValue(tempMap)
    }

    fun resetList() {
        _frequency.postValue(results.value)
    }

}