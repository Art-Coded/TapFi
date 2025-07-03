package com.example.wificardgenerator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wificardgenerator.ui.theme.Purple40
import kotlinx.coroutines.launch
import kotlin.math.max
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


@Composable
fun WifiScreen() {
    val pageCount = 3
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })

    val icons = listOf(
        R.drawable.ic_wifi3,
        R.drawable.ic_customize,
        R.drawable.ic_preview
    )

    Column(modifier = Modifier.fillMaxSize()) {
        val currentPage = pagerState.currentPage

        AnimatedContent(
            targetState = currentPage == 0,
            transitionSpec = {
                if (targetState) {
                    (fadeIn(tween(150)) + slideInVertically { -it / 2 }).togetherWith(
                        fadeOut(tween(150)) + slideOutVertically { it / 2 }
                    )
                } else {
                    (fadeIn(tween(150)) + slideInVertically { it / 2 }).togetherWith(
                        fadeOut(tween(150)) + slideOutVertically { -it / 2 }
                    )
                }.using(SizeTransform(clip = false))
            },
            label = "HeaderTransition"
        ) { isVisible ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isVisible) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_wifi3),
                            contentDescription = "App Logo",
                            modifier = Modifier.height(42.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "TapFi",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                CustomPagerIndicator(
                    pageCount = pageCount,
                    pagerState = pagerState,
                    icons = icons,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> SlideOne()
                1 -> SlideTwo()
                2 -> SlideThree()
            }
        }
    }
}

@Composable
fun CustomPagerIndicator(
    pageCount: Int,
    pagerState: PagerState,
    icons: List<Int>,
    modifier: Modifier = Modifier
) {
    val currentPage = pagerState.currentPage
    val offsetFraction = pagerState.currentPageOffsetFraction

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (index <= currentPage)
                            Purple40
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icons[index]),
                    contentDescription = "Step ${index + 1}",
                    tint = if (index <= currentPage)
                        Color.White
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }


            if (index < pageCount - 1) {
                val lineProgress = when {
                    currentPage > index -> 1f // fully filled
                    currentPage == index -> max(0f, offsetFraction) // partial while dragging forward
                    currentPage == index + 1 -> 1f - offsetFraction // partial while dragging back
                    else -> 0f
                }

                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    // Base line
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                    // Animated fill
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(lineProgress)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}