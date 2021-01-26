package util

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
class NavigationBackstack<NavigationScreen>(
    startDestination : NavigationScreen
){

    private var screenList : List<NavigationScreen> by mutableStateOf(listOf(startDestination))


    fun currentScreeen() : NavigationScreen = screenList.last()


    fun popBackStack() {
        screenList = screenList.toMutableList().also {
            it.removeLast()
        }
    }


    fun addToStack(newScreen : NavigationScreen) {
        screenList = screenList.toMutableList().also {
            it.add(newScreen)
        }
    }

    fun isNull() = screenList.isEmpty()
}