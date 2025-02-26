package com.madhuresh.supermarket

sealed class Screens(val screen: String){
    data object Home: Screens("home")
    data object Explore: Screens("explore")
    data object Cart: Screens("cart")
    data object Account: Screens("account")
}