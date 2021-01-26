package presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import util.imageFromFile
import java.io.File
import javax.swing.JFileChooser

@Composable
fun ImagePicker(
    startingImage : String,
    pickedImage : (imageFilePath : String) -> Unit,
    onNext : () -> Unit,
    onBack : (() -> Unit)? = null
){
    var file by remember { mutableStateOf(startingImage) }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(10.dp)
    ) {
        if (file.isBlank()) {
            Box(
                modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.8f).align(Alignment.CenterHorizontally).background(Color.Red)
            )
        } else {
            Image(
                bitmap = imageFromFile(File(file)) ,
                modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.8f).align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Inside
            )
        }
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Text(file , modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            onBack?.let{ it ->
                Button(
                    onClick = {
                        it()
                    },
                    content =  {
                        Text("Back")
                    },
                )
            }


            Button(
                onClick = {
                    val fileChooser = JFileChooser()
                    val result = fileChooser.showOpenDialog(null)
                    if (result == JFileChooser.APPROVE_OPTION) {
                        file= fileChooser.selectedFile.absolutePath
                        pickedImage(file)
                    }
                },
                content =  {
                    Text(if(file.isBlank()) "Open image" else "Change Image")
                },
            )
            Button(
                onClick = onNext,
                content = {
                    Text("Next")
                },
                enabled = file.isNotBlank()
            )

        }

    }
}