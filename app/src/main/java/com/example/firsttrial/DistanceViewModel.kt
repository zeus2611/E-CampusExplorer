package com.example.firsttrial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DistanceViewModel : ViewModel() {
    private val _distance = MutableLiveData<Double>()
    val distance: LiveData<Double>
        get() = _distance

    fun updateDistance(newDistance: Double) {
        _distance.value = newDistance
    }
}
