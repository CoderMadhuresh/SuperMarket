package com.madhuresh.supermarket.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.madhuresh.supermarket.dataModel.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFilterProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    init {
        fetchProductsFromAllCategories()
    }

    private fun fetchProductsFromAllCategories() {
        viewModelScope.launch {
            val productList = mutableListOf<Product>()

            database.child("categories").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (categorySnapshot in snapshot.children) {
                        val productsSnapshot = categorySnapshot.child("products")
                        for (productSnapshot in productsSnapshot.children) {
                            val product = productSnapshot.getValue(Product::class.java)
                            product?.let { productList.add(it) }
                        }
                    }
                    _products.value = productList
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}