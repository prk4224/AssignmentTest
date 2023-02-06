package com.jaehong.presentation.ui.screen.search.header

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehong.presentation.util.Constants.RECENT_SEARCH_TEXT
import com.jaehong.presentation.util.Constants.SEARCH_TEXT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderItem(
    modifier: Modifier,
    text: String,
    setText: (String) -> Unit,
    searchOnClicked: () -> Unit,
    recentOnClicked: () -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
       TextField(
           value = text,
           onValueChange = setText,
           maxLines = 1,

           modifier = Modifier
               .weight(4f)
       )

        Spacer(modifier = Modifier.size(5.dp))
        
        Button(
            onClick = { searchOnClicked() },
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .fillMaxSize()
                .weight(1.5f)

        ) {
            Text(text = SEARCH_TEXT, color = Color.Black)
        }
        Spacer(modifier = Modifier.size(5.dp))

        Button(
            onClick = { recentOnClicked() },
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .fillMaxSize()
                .weight(1.5f)
        ) {
            Text(text = RECENT_SEARCH_TEXT, color = Color.Black)
        }
    }
}


