package com.example.dessertclicker.data


import com.example.dessertclicker.R
import com.example.dessertclicker.model.Dessert

/**
 * [Datasource] generates a list of [Dessert]
 */
object Datasource {
    val dessertList = listOf(
        Dessert(R.drawable.cupcake, 5, 2),
        Dessert(R.drawable.donut, 10, 5),
        Dessert(R.drawable.eclair, 15, 4),
        Dessert(R.drawable.froyo, 18, 1),
        Dessert(R.drawable.gingerbread, 16, 10),
        Dessert(R.drawable.honeycomb, 28, 20),
        Dessert(R.drawable.icecreamsandwich, 25, 12),
        Dessert(R.drawable.jellybean, 32, 6),
        Dessert(R.drawable.kitkat, 12, 9),
        Dessert(R.drawable.lollipop, 10, 7),
        Dessert(R.drawable.marshmallow, 23, 1),
        Dessert(R.drawable.nougat, 45, 5),
        Dessert(R.drawable.oreo, 3, 7)
    )
}