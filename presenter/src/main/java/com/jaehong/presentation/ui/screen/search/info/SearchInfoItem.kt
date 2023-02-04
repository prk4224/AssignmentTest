package com.jaehong.presentation.ui.screen.search.info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehong.domain.model.MovieItem
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SearchInfoItem(
    item: MovieItem,
    itemOnClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { itemOnClicked() }
    ) {
        GlideImage(
            imageModel = item.image,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .size(150.dp)
                .padding(10.dp)
                .background(Color.Black)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .weight(4f)
        ) {
            Text(text = "제목: ${item.title}")
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = "출시: ${item.pubDate}")
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = "평점: ${item.userRating}")
        }
    }
}