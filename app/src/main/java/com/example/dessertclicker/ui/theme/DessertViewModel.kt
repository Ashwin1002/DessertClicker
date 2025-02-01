package com.example.dessertclicker.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.determineDessertToShow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {

    private  val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    fun dessertPressed() {
       _uiState.update { cupcakeState ->
           val soldDessertsCount = cupcakeState.desertSold + 1
           val nextIndex = findDessertIndex(soldDessertsCount)
           cupcakeState.copy(
               currentDessertIndex = nextIndex,
               revenue = cupcakeState.revenue + cupcakeState.currentDessertPrice,
               desertSold = soldDessertsCount,
               currentDessertImageId = dessertList[nextIndex].imageRes,
               currentDessertPrice = dessertList[nextIndex].price
           )
       }

    }


    private fun findDessertIndex(dessertSoldCount: Int): Int {
        var initialIndex = 0
        for (index in dessertList.indices) {
            if(dessertSoldCount >= dessertList[index].startProductionAmount){
                initialIndex = index
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more
                // desserts, you'll start producing more expensive desserts as determined by
                // startProductionAmount. We know to break as soon as we see a dessert who's
                // "startProductionAmount" is greater than the amount sold.
                break
            }
        }
        return  initialIndex
    }
}