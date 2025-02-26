package com.madhuresh.supermarket.dataModel

data class Order(
    val userId: String,
    val products: List<OrderProduct>,
    val address: String,
    val totalPrice: Int,
    val dateTime: String,
    var orderStatus: String = "Processing"
)

data class OrderProduct(
    val name: String = "",
    val quantity: Int = 0,
    val price: Int = 0
)

data class UserOrder(
    val dateTime: String = "",
    val products: List<OrderProduct> = emptyList(),
    val totalPrice: Int = 0,
    var orderStatus: String = "Processing"
)