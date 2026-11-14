package com.example.uaspemrogrammanmobile.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspemrogrammanmobile.data.repository.AuthRepository
import com.example.uaspemrogrammanmobile.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableLiveData<Resource<FirebaseUser>>()
    val authState: LiveData<Resource<FirebaseUser>> = _authState

    fun isUserLoggedIn(): Boolean = repository.getCurrentUser() != null

    fun login(email: String, pass: String) {
        _authState.value = Resource.Loading
        viewModelScope.launch {
            _authState.value = repository.login(email, pass)
        }
    }

    fun register(name: String, email: String, pass: String) {
        _authState.value = Resource.Loading
        viewModelScope.launch {
            _authState.value = repository.register(name, email, pass)
        }
    }
}