package com.felix.ventral_android.ui.screens.search

import androidx.lifecycle.ViewModel
import com.felix.ventral_android.data.local.LocalDataStore
import com.felix.ventral_android.domain.repository.EventRepository
import com.felix.ventral_android.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val localDataStore: LocalDataStore
) : ViewModel() {

}