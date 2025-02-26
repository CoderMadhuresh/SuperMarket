package com.madhuresh.supermarket.dataModel

data class Product(
    val desc: String = "",
    val image: String = "",
    val ingredients: String = "",
    val images: Map<String, String> = mapOf(),
    val manufacturer: String = "",
    val name: String = "",
    val price: Int = 0,
    val shelf: String = "",
    val stock: Int = 0,
    val unit: String = ""
)
