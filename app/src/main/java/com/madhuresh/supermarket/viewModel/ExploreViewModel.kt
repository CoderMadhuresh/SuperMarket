package com.madhuresh.supermarket.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.madhuresh.supermarket.dataModel.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("categories")
    private val _categories = MutableStateFlow<Map<String, Category>>(emptyMap())
    val categories: StateFlow<Map<String, Category>> = _categories

    init {
        viewModelScope.launch {
            database.get().addOnSuccessListener { snapshot ->
                val categoriesMap = snapshot.children.associate { child ->
                    val categoryName = child.key ?: ""
                    val category = child.getValue(Category::class.java) ?: Category()
                    categoryName to category
                }
                _categories.value = categoriesMap
            }
        }
    }
}
