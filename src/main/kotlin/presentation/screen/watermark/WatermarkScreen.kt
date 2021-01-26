package presentation.screen.watermark

import ImageProcessor.Resource
import ImageProcessor.watermark.Watermark
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import presentation.component.ImagePicker
import presentation.screen.stenography.*
import util.NavigationBackstack
import util.imageFromByteInputStream
import java.io.ByteArrayInputStream

sealed class WatermarkNavigation() {
    object NavigateToBaseImagePicker : WatermarkNavigation()
    object NavigateToWatermarkImagePicker : WatermarkNavigation()
    object NavigateToWatermarkProcess : WatermarkNavigation()
}

@Composable
fun WatermarkScreen(
    onNavigationStackNull : () -> Unit
) {


    var watermarkNavigation by remember {
        mutableStateOf(NavigationBackstack<WatermarkNavigation>(WatermarkNavigation.NavigateToBaseImagePicker))
    }

    var currentScreenText by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false)}


    var baseImageFile by remember { mutableStateOf("") }
    var watermarkImageFile by remember { mutableStateOf("") }
    val watermark by remember { mutableStateOf(Watermark()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        if(isLoading){
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp).align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    modifier = Modifier.height(24.dp).clickable {
                        if(!watermarkNavigation.isNull()){
                            watermarkNavigation.popBackStack()
                        }else{
                            onNavigationStackNull()
                        }
                    }.align(Alignment.CenterStart)
                )
                Text(
                    text = currentScreenText,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            when (val currentScreen = watermarkNavigation.currentScreeen()) {
                is WatermarkNavigation.NavigateToBaseImagePicker -> {
                    currentScreenText = "Pilih gambar yang ingin anda berikan watermark"
                    ImagePicker(
                        startingImage = baseImageFile,
                        pickedImage = {
                            baseImageFile = it
                        },
                        onNext = {
                            watermarkNavigation.addToStack(WatermarkNavigation.NavigateToWatermarkImagePicker)
                        }
                    )
                }
                is WatermarkNavigation.NavigateToWatermarkImagePicker -> {
                    currentScreenText = "Pilih gambar yang ingin anda jadikan watermark"
                    ImagePicker(
                        startingImage = watermarkImageFile,
                        pickedImage = {
                            watermarkImageFile = it
                        },
                        onNext = {
                            watermarkNavigation.addToStack(WatermarkNavigation.NavigateToWatermarkProcess)
                        }
                    )
                }
                is WatermarkNavigation.NavigateToWatermarkProcess -> {
                    WatermarkProcess(
                        baseImage = baseImageFile,
                        watermarkImage = watermarkImageFile,
                        processor = watermark
                    )
                }
            }
        }
    }
}

@Composable
fun WatermarkProcess(
    baseImage : String,
    watermarkImage : String,
    processor : Watermark
){
    var watermarkJob : Job by remember { mutableStateOf(Job())}
    var image : ByteArrayInputStream? by remember { mutableStateOf(null)}
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.align(Alignment.Center)
        ){
            image?.let{
                Image(
                    bitmap = imageFromByteInputStream(it),
                    modifier = Modifier.size(240.dp)
                )
            }
            Button(
                {
                    watermarkJob = CoroutineScope(Main).launch {
                      processor.run(
                          baseImage = baseImage,
                          watermarkImage = watermarkImage,
                          dispatcher = IO
                      ).collect { result ->
                          when(result){
                              is Resource.Success -> {
                                  println("Success")
                                  result.data?.let{
                                      image = it
                                  }

                              }
                              is Resource.Loading -> {
                                  println("Gagal")
                              }
                              is Resource.Error -> {
                                  println(result.message ?: "Wow")
                              }
                          }
                      }
                    }
                }
            ){
                Text("Watermark Sekarang")
            }
        }
    }
}