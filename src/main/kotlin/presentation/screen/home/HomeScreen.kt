package presentation.screen.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import model.ImageProcessor

@Composable
fun HomeScreen(
    onItemClick: (item: ImageProcessor) -> Unit
) {
    val items = listOf(
        ImageProcessor.Watermark(),
        ImageProcessor.Stenography(),
        ImageProcessor.Noise()
    )
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(24.dp)
    ) {
        Text(
            text = "Pilih Image Processing yang ingin anda gunakan",
            style = MaterialTheme.typography.h5
        )
        Spacer(Modifier.height(20.dp))
        MenuCaraousel(
            items = items,
            onItemClick = onItemClick
        )

    }
}

@Composable
fun MenuCaraousel(
    items: List<ImageProcessor>,
    modifier: Modifier = Modifier,
    onItemClick: (item: ImageProcessor) -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val state = rememberScrollState(0f)
        ScrollableColumn(
            modifier = Modifier.fillMaxHeight(),
            state
        ) {
            items.forEach { item ->
                CarouselItem(item = item, onItemClick = onItemClick)
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
        }
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(state)
        )
    }


}

@Composable
fun CarouselItem(
    modifier: Modifier = Modifier,
    item: ImageProcessor,
    onItemClick: (item: ImageProcessor) -> Unit
) {

    Card(
        modifier = Modifier.clickable { onItemClick(item) }.fillMaxWidth(),
        elevation = 5.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Image(
                bitmap = imageFromResource("images/${item.imagePath}"),
                modifier = Modifier.clip(RoundedCornerShape(5.dp)).height(64.dp).width(64.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                text = item.name,
                style = MaterialTheme.typography.subtitle1
            )
        }
    }

}