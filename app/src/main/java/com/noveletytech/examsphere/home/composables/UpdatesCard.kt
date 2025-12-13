package com.noveletytech.examsphere.home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpdatesCard(
    modifier: Modifier = Modifier,
    imageUrls: List<String>,
    animationSpeed: Long = 9000 // Dynamic speed parameter
) {
    if (imageUrls.isNotEmpty()) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (imageUrls.size == 1) {
                    AsyncImage(
                        model = imageUrls.first(),
                        contentDescription = "Update Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

                    // Auto-scroll effect using dynamic speed
                    LaunchedEffect(animationSpeed) { // Restart effect if speed changes
                        while (true) {
                            delay(animationSpeed) 
                            yield()
                            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        AsyncImage(
                            model = imageUrls[page],
                            contentDescription = "Update Image ${page + 1}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Page indicators
                    Row(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color = if (pagerState.currentPage == iteration) Color.White else Color.Gray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
