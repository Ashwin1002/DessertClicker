package com.example.dessertclicker.model

import androidx.annotation.DrawableRes

data class Dessert(
    @DrawableRes val imageRes: Int,
    val price: Int,
    val startProductionAmount: Int,
)
