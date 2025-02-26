package com.madhuresh.supermarket.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.madhuresh.supermarket.dataModel.Product
import com.madhuresh.supermarket.CartDatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val cartDatabaseHelper = CartDatabaseHelper(application)

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _cartItems = mutableStateListOf<Product>()
    val cartItems: List<Product> get() = _cartItems

    private val _addedState = mutableStateMapOf<String, Boolean>()
    val addedState: Map<String, Boolean> get() = _addedState

    init {
        loadCartItems()
    }

    fun fetchProductsByCategory(category: String) {
        viewModelScope.launch {
            val productsRef = database.child("categories").child(category).child("products")

            productsRef.get().addOnSuccessListener { dataSnapshot ->
                val productList = mutableListOf<Product>()
                for (snapshot in dataSnapshot.children) {
                    val product = snapshot.getValue(Product::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                _products.value = productList
            }
                .addOnFailureListener {
                _products.value = emptyList()
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            if (!_cartItems.contains(product)) {
                cartDatabaseHelper.addProduct(product)
                _cartItems.add(product)
                _addedState[product.name] = true
            }
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            if (_cartItems.contains(product)) {
                cartDatabaseHelper.removeProduct(product)
                _cartItems.remove(product)
                _addedState[product.name] = false
            }
        }
    }

    fun loadCartItems() {
        viewModelScope.launch {
            _cartItems.clear()
            _cartItems.addAll(cartDatabaseHelper.getAllProducts())
            _addedState.clear()
            _cartItems.forEach { _addedState[it.name] = true }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            _cartItems.clear()
            _addedState.clear()
            cartDatabaseHelper.clearCart()
        }
    }
}