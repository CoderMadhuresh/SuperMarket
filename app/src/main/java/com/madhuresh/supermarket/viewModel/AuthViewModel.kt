package com.madhuresh.supermarket.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.madhuresh.supermarket.dataModel.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> get() = _isAuthenticated

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    init {
        val currentUser = auth.currentUser
        _isAuthenticated.value = currentUser != null && currentUser.isEmailVerified
        currentUser?.let {
            fetchUserData(it)
        }
    }

    fun signIn(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    _isAuthenticated.value = true
                    fetchUserData(user)
                    Toast.makeText(context, "Sign-in successful", Toast.LENGTH_LONG).show()
                } else {
                    auth.signOut()
                    Toast.makeText(context, "Email not verified\nPlease verify your email.", Toast.LENGTH_LONG).show()
                }
            } else {
                _isAuthenticated.value = false
                Toast.makeText(context, "Sign-in failed\nPlease check your credentials", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun register(name: String, email: String, password: String, phone: String, address: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val userId = it.uid
                    val userData = User(userId, name, email, address, phone)
                    database.child(userId).setValue(userData).addOnCompleteListener { saveTask ->
                        if (saveTask.isSuccessful) {
                            Toast.makeText(context, "Registration successful\nSign in with you credentials", Toast.LENGTH_LONG).show()
                            sendEmailVerification(it, context)
                        }
                        else {
                            Toast.makeText(context, "Failed to save user data\nPlease try again", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            else {
                Toast.makeText(context, "Registration failed\nPlease try again", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchUserData(firebaseUser: FirebaseUser) {
        val userId = firebaseUser.uid
        database.child(userId).get().addOnSuccessListener { snapshot ->
            _user.value = snapshot.getValue(User::class.java)
        }
    }

    private fun sendEmailVerification(user: FirebaseUser, context: Context) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Verification email sent\nPlease check your email", Toast.LENGTH_LONG).show()
                auth.signOut()
            }
            else {
                Toast.makeText(context, "Failed to send verification email\nPlease try again", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun signOut(context: Context) {
        auth.signOut()
        _isAuthenticated.value = false
        _user.value = null
        Toast.makeText(context, "Signed out successfully", Toast.LENGTH_LONG).show()
    }

    fun updateAddress(newAddress: String, context: Context) {
        val userId = auth.currentUser?.uid ?: return
        database.child(userId).child("address").setValue(newAddress).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _user.value = _user.value?.copy(address = newAddress)
                Toast.makeText(context, "Address updated successfully", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Failed to update address", Toast.LENGTH_LONG).show()
            }
        }
    }
}
