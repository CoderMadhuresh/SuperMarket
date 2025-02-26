package com.madhuresh.supermarket.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.madhuresh.supermarket.dataModel.Product

class HomeProductViewModel : ViewModel() {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val _products = MutableStateFlow<Map<String, Map<String, Product>>>(emptyMap())
    val products: StateFlow<Map<String, Map<String, Product>>> = _products

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            database.child("home").get().addOnSuccessListener { snapshot ->
                val data = snapshot.children.associate { categorySnapshot ->
                    val categoryName = categorySnapshot.key ?: ""
                    val products = categorySnapshot.children.associate { productSnapshot ->
                        val productKey = productSnapshot.key ?: ""
                        val product = productSnapshot.getValue(Product::class.java) ?: Product()
                        productKey to product
                    }
                    categoryName to products
                }
                _products.value = data
            }.addOnFailureListener {}
        }
    }
}