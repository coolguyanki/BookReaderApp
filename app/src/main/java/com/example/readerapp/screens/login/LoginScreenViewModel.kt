package com.example.readerapp.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapp.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
//    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInWithEmailAndPassword(email:String, password: String, home:() -> Unit)
    = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d("FB","signInWithEmailAndPassword: YAYYY ${task.result})}")
                        home()
                } else {
                    Log.d("FB","signInWithEmailAndPassword: ${task.result}")
                }
                } .addOnFailureListener {exception ->
                    _loading.value = false
                    Log.e("FB", "signInWithEmailAndPassword: Error", exception)
                }

        } catch (ex: Exception) {
            Log.d("FB","error message: ${ex.message}")

        }

    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    } else {
                        Log.d("ANKIT","createUserWithEmailAndPassword: ${task.result}")

                    }
                } .addOnFailureListener {exception ->
                _loading.value = false
                Log.e("FB", "createUserWithEmailAndPassword: Error", exception)
            }
        }

    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        // to save the users in FireStore database
        val user = MUser(userId = userId.toString(),
            displayName = displayName.toString(),
            profileImage = "",
            quote = "Life is fucked up",
            profession = "Android Developer",
            id = null).toMap()


        FirebaseFirestore.getInstance().collection("users")
            .add(user)

    }

}