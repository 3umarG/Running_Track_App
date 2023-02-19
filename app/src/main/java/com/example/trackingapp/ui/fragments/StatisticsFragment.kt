package com.example.trackingapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.trackingapp.R
import com.example.trackingapp.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val viewModel : StatisticsViewModel by viewModels()
}