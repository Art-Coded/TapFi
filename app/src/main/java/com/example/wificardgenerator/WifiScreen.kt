package com.example.wificardgenerator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wificardgenerator.ui.theme.Purple40
import kotlin.math.max
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ColorFilter
import com.example.wificardgenerator.Database.SharedViewModel
import com.example.wificardgenerator.ui.theme.dark_mode
import kotlinx.coroutines.launch

@Composable
fun WifiScreen(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    onToggleTheme: (Boolean) -> Unit = {},
    colorPickerClick: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    val pageCount = 3
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })

    val icons = listOf(
        R.drawable.ic_wifi3,
        R.drawable.ic_customize,
        R.drawable.ic_preview
    )

    val coroutineScope = rememberCoroutineScope()
    fun navigateToSlide(index: Int) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(
                page = index,
                animationSpec = tween(durationMillis = 500)
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val currentPage = pagerState.currentPage

        AnimatedContent(
            targetState = currentPage == 0,
            transitionSpec = {
                if (targetState) {
                    (fadeIn(tween(250)) + slideInVertically { -it / 2 }).togetherWith(
                        fadeOut(tween(250)) + slideOutVertically { it / 2 }
                    )
                } else {
                    (fadeIn(tween(250)) + slideInVertically { it / 2 }).togetherWith(
                        fadeOut(tween(250)) + slideOutVertically { -it / 2 }
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
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // App logo
                        Image(
                            painter = painterResource(id = R.drawable.ic_wifi3),
                            contentDescription = "App Logo",
                            modifier = Modifier.height(42.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "TapFi",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            painter = painterResource(
                                id = if (isDarkTheme) R.drawable.dark_mode else R.drawable.light_mode
                            ),
                            contentDescription = "Theme Icon",
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 6.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { onToggleTheme(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White
                            )
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
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            when (page) {
                0 -> SlideOne(onNextClick = { navigateToSlide(1) }, sharedViewModel = sharedViewModel)
                1 -> SlideTwo(
                    colorPickerClick = { colorPickerClick() },
                    sharedViewModel = sharedViewModel,
                    onBackClick = { navigateToSlide(0) },
                    onNextClick = { navigateToSlide(2) }
                )
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
                            MaterialTheme.colorScheme.primary
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
                    currentPage > index -> 1f
                    currentPage == index -> max(0f, offsetFraction)
                    currentPage == index + 1 -> 1f - offsetFraction
                    else -> 0f
                }

                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
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