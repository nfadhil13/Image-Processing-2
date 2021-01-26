package presentation.screen.stenography

import ImageProcessor.Resource
import ImageProcessor.stenography.StenograpyhDecode
import ImageProcessor.stenography.StenograpyhEncode
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import presentation.component.ImagePicker
import presentation.screen.stenography.StenographyNavigation.*
import util.NavigationBackstack
import util.errorHandler
import util.imageFromByteInputStream
import util.imageFromFile
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import java.awt.image.BufferedImage





sealed class StenographyNavigation() {
    object NavigatToChoice : StenographyNavigation()
    object NavigateToImagePicker : StenographyNavigation()
    object NavigateToDecodeScreen : StenographyNavigation()
    object NavigateToEncoder : StenographyNavigation()
    data class NavigatoToSuccessEncode(val byteArrayInputStream: ByteArrayInputStream) : StenographyNavigation()
}

@Composable
fun Stenography(
    onNavigationStackNull: () -> Unit
) {
    var file by remember { mutableStateOf("") }
    var encoder by remember { mutableStateOf(StenograpyhEncode()) }
    var decoder by remember { mutableStateOf(StenograpyhDecode()) }
    var currentScreenText by remember { mutableStateOf("")}
    var isLoading by remember { mutableStateOf(false)}

    var stenographyNavigation by remember {
        mutableStateOf(NavigationBackstack<StenographyNavigation>(NavigateToImagePicker))
    }

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
                        if(!stenographyNavigation.isNull()){
                            stenographyNavigation.popBackStack()
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
            when (val currentScreen = stenographyNavigation.currentScreeen()) {
                is NavigateToImagePicker -> {
                    currentScreenText = "Silahkan ambil gambar yang kamu mau"
                    ImagePicker(
                        startingImage = file,
                        pickedImage = {
                            file = it
                        },
                        onNext = {
                            stenographyNavigation.addToStack(NavigatToChoice)
                        },
                        onBack = onNavigationStackNull
                    )
                }
                is NavigatToChoice -> {
                    currentScreenText = "Apa yang ingin kamu lakukan?"
                    ChoiceScreen(
                        onDecodeClicked = { stenographyNavigation.addToStack(NavigateToDecodeScreen) },
                        onEncodeClicked = { stenographyNavigation.addToStack(NavigateToEncoder) }
                    )
                }
                is NavigateToDecodeScreen -> {
                    currentScreenText = "Ayo decode gambar kamu"
                    DecodeScreen(
                        imageFilePath = file,
                        onLoading = {
                            isLoading = it
                        },
                        decoder = decoder
                    )
                }
                is NavigateToEncoder -> {
                    currentScreenText = "Ayo encode gambar kamu"
                    EncodeScreen(
                        imageFilePath = file,
                        encoder = encoder,
                        onEncodeSucces = { byteArray ->
                            isLoading = false
                            stenographyNavigation.addToStack(StenographyNavigation.NavigatoToSuccessEncode(byteArray))
                        },
                        onLoading = {
                            isLoading = true
                        }
                    )
                }
                is NavigatoToSuccessEncode -> {
                    currentScreenText = "Encoding Selesai"
                    EncodeSuccessScreen(
                        byteArrayInputStream = currentScreen.byteArrayInputStream,
                        onLoading = {
                            isLoading = true
                        },
                        onSucces = {
                            isLoading = false
                        }
                    )
                }

            }
        }
    }




}

@Composable
fun ChoiceScreen(
    onEncodeClicked: () -> Unit,
    onDecodeClicked: () -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxHeight().weight(0.5f)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.65f).aspectRatio(1f)
                    ) {
                        Image(
                            bitmap = imageFromResource("images/encode.png"),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.65f),
                            text = "Masukan pesan tersembunyi pada gambar yang kamu pilih",
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = onEncodeClicked
                    ) {
                        Text(
                            text = "Encode"
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxHeight().weight(0.5f)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.65f).aspectRatio(1f)
                    ) {
                        Image(
                            bitmap = imageFromResource("images/decode.png"),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.65f),
                            text = "Dapatkan pesan dari gambar yang pernah kamu encode",
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onDecodeClicked,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Decode"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DecodeScreen(
    imageFilePath: String,
    decoder : StenograpyhDecode,
    onLoading : (isLoading : Boolean) -> Unit
) {
    var decodeJob : Job by remember { mutableStateOf(Job())}

    var decodedString  by remember { mutableStateOf("")}

    var keyState by remember { mutableStateOf("") }


    onDispose {
        if(decodeJob.isActive){
            decodeJob.cancel()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                bitmap = imageFromFile(File(imageFilePath)),
                modifier = Modifier.weight(0.45f).fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Box(
                modifier = Modifier.weight(0.45f).fillMaxHeight()
            ) {
                Text(
                    text = decodedString,
                    modifier = Modifier.fillMaxWidth(0.75f)
                )
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(
                        value = keyState,
                        onValueChange = {
                            if (it.isBlank() || it.length == 1) {
                                keyState = it
                            }
                        },
                        isErrorValue = keyState.length > 1,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            decodeJob = CoroutineScope(Main).launch {
                                decoder.run(
                                    imgFilePath = imageFilePath,
                                    stopChar = keyState[0],
                                    dispatcher = IO
                                ).collect { result ->
                                    when(result) {
                                        is Resource.Success -> {
                                            result.data?.let{
                                                decodedString = it
                                            }
                                        }
                                        is Resource.Loading -> {
                                            onLoading(false)
                                        }
                                        is Resource.Error -> {
                                            println(result.message ?: "Wow")
                                        }
                                    }
                                }
                            }
                        }
                    ){
                        Text("Encode!")
                    }
                }


            }
        }
    }
}


@Composable
fun EncodeScreen(
    imageFilePath: String,
    encoder: StenograpyhEncode,
    onEncodeSucces : (result : ByteArrayInputStream)  -> Unit,
    onLoading : () -> Unit
) {

    var textState by remember { mutableStateOf("") }

    var keyState by remember { mutableStateOf("") }

    var encodeJob : Job by remember { mutableStateOf(Job())}


    onDispose {
        if(encodeJob.isActive){
            encodeJob.cancel()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                bitmap = imageFromFile(File(imageFilePath)),
                modifier = Modifier.weight(0.45f).fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Box(
                modifier = Modifier.weight(0.45f).fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    TextField(
                        value = textState,
                        onValueChange = { newString -> textState = newString },
                        isErrorValue = if(keyState.isNotBlank()) textState.contains(keyState[0]) else false,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(
                        value = keyState,
                        onValueChange = {
                            if (it.isBlank() || it.length == 1) {
                                keyState = it
                            }
                        },
                        isErrorValue = keyState.length > 1,
                        maxLines = 1
                        )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            encodeJob = CoroutineScope(Main).launch {
                                encoder.run(
                                    message = textState,
                                    filePath = imageFilePath,
                                    stopCharacter = keyState[0],
                                    dispatcher = IO
                                ).collect { result ->
                                    when(result) {
                                        is Resource.Success -> {
                                            result.data?.let{
                                                onEncodeSucces(it)
                                            }
                                        }
                                        is Resource.Loading -> {
                                            onLoading()
                                        }
                                        is Resource.Error -> {
                                            println(result.message ?: "Wow")
                                        }
                                    }
                                }
                            }
                        }
                    ){
                        Text("Encode!")
                    }
                }


            }
        }
    }
}


@Composable
fun EncodeSuccessScreen(
    byteArrayInputStream: ByteArrayInputStream,
    onLoading: () -> Unit,
    onSucces : () -> Unit
){


    var saveJob : Job by remember { mutableStateOf(Job())}

    onDispose {
        saveJob.cancel()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(32.dp))
//        Image(
//            bitmap = imageFromByteInputStream(byteArrayInputStream),
//            modifier = Modifier.size(124.dp),
//            contentScale = ContentScale.Fit
//        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick =  {
                val fileChooser = JFileChooser()
                fileChooser.dialogTitle = "Simpan gambar"
                val result = fileChooser.showSaveDialog(null)
                if(result == JFileChooser.APPROVE_OPTION){
                    saveJob = CoroutineScope(IO).launch {
                        onLoading()
                        try{
                            val bi: BufferedImage = ImageIO.read(byteArrayInputStream)
                            val outputfile = fileChooser.selectedFile
                            ImageIO.write( bi, "png", outputfile)
                            onSucces()
                        }catch(exception : Exception){
                            println(errorHandler(exception))
                        }
                    }
                }
            }
        ){
            Text("Save")
        }
    }
}
