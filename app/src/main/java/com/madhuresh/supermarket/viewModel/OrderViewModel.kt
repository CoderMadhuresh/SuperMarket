package com.madhuresh.supermarket.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.madhuresh.supermarket.dataModel.UserOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val _ordersSnapshot = MutableStateFlow<List<Pair<String, UserOrder>>>(emptyList())
    val ordersSnapshot: StateFlow<List<Pair<String, UserOrder>>> = _ordersSnapshot

    fun fetchOrders(userId: String) {
        val database = FirebaseDatabase.getInstance()
        val ordersRef = database.getReference("users/$userId/orders")

        viewModelScope.launch {
            ordersRef.get().addOnSuccessListener { snapshot: DataSnapshot ->
                val ordersList = snapshot.children.mapNotNull { childSnapshot ->
                    val order = childSnapshot.getValue<UserOrder>()
                    if (order != null) {
                        childSnapshot.key?.let { orderId ->
                            orderId to order
                        }
                    } else {
                        null
                    }
                }
                _ordersSnapshot.value = ordersList
            }
        }
    }

    fun updateOrderStatus(orderId: String, userId: String, newStatus: String, context: Context) {
        val ordersRef = FirebaseDatabase.getInstance().reference.child("orders").child(orderId)
        val userOrderRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("orders").child(orderId)

        ordersRef.child("orderStatus").setValue(newStatus).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userOrderRef.child("orderStatus").setValue(newStatus).addOnCompleteListener { userTask ->
                    if (userTask.isSuccessful) {
                        Toast.makeText(context, "Your order has been cancelled", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update order status", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(context, "Failed to update order status", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
