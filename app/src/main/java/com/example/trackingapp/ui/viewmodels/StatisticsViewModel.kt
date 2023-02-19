package com.example.trackingapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.trackingapp.data.repository.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val repo: RunRepository) : ViewModel() {
}