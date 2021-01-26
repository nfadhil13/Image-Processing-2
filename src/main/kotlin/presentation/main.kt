import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import model.ImageProcessor
import nu.pattern.OpenCV
import presentation.screen.home.HomeScreen
import presentation.screen.stenography.Stenography
import theme.MyTheme
import util.NavigationBackstack

fun main() = Window {

    var mainNavigation by remember { mutableStateOf(NavigationBackstack<MainScreen>(MainScreen.HomeMainScreen)) }

    CoroutineScope(IO).launch {
        OpenCV.loadLocally()
        println("Load OpenCV selesai")
    }

    MyTheme(
        darkTheme = true
    ){
        Surface(
            modifier = Modifier.fillMaxSize()
        ){
            when(mainNavigation.currentScreeen()){
                is MainScreen.HomeMainScreen -> {
                    HomeScreen(
                        onItemClick = {
                            when(it){
                                is ImageProcessor.Stenography -> {
                                    mainNavigation.addToStack(newScreen = MainScreen.StenographyMainScreen)
                                }
                                else -> {

                                }
                            }
                        }
                    )
                }
                is MainScreen.StenographyMainScreen-> {
                    Stenography(onNavigationStackNull = {
                        mainNavigation.popBackStack()
                    })
                }
            }
        }
    }

}


sealed class MainScreen(){
    object HomeMainScreen : MainScreen()
    object StenographyMainScreen : MainScreen()
}
