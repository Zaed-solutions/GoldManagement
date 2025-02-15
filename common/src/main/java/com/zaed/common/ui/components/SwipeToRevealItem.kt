package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.overscroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

enum class SwipeState { Left, Resting, Right }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToRevealItem(
    modifier: Modifier = Modifier,
    secondActionIcon: ImageVector? = null,
    onClickDelete: () -> Unit = {},
    onSecondActionClicked: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val isRTL = layoutDirection == LayoutDirection.Rtl  

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val dragState = remember {
        val actionOffset = with(density) { 100.dp.toPx() }

        AnchoredDraggableState(
            initialValue = SwipeState.Resting,
            anchors = DraggableAnchors {
                if (isRTL) {
                    SwipeState.Left at actionOffset
                    SwipeState.Resting at 0f
                    SwipeState.Right at -actionOffset
                } else {
                    SwipeState.Left at -actionOffset
                    SwipeState.Resting at 0f
                    SwipeState.Right at actionOffset
                }
            },
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec,
        )
    }
    LaunchedEffect(dragState) {
        snapshotFlow { dragState.settledValue }
            .collectLatest {
                when (it) {
                    SwipeState.Right ->  {
                        if (isRTL) {
                            onClickDelete()
                        }else{
                            onSecondActionClicked()
                        }
                    }
                    SwipeState.Left -> {
                        if (isRTL) {
                            onSecondActionClicked()
                        }else{
                            onClickDelete()
                        }
                    }
                    else -> {}
                }
                delay(30)
                dragState.animateTo(SwipeState.Resting)
            }
    }
    val overScrollEffect = ScrollableDefaults.overscrollEffect()

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = dragState.currentValue == SwipeState.Left,
                enter = slideInHorizontally(animationSpec = tween()) { it },
                exit = slideOutHorizontally(animationSpec = tween()) { it }
            ) {
                if (secondActionIcon != null) {
                    Surface (
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            modifier = Modifier.padding(8.dp),
                            imageVector = secondActionIcon,
                            contentDescription = "secondAction"

                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = dragState.currentValue == SwipeState.Right,
                enter = slideInHorizontally(animationSpec = tween()) { -it },
                exit = slideOutHorizontally(animationSpec = tween()) { -it }
            ) {
                Surface (
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete"
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .anchoredDraggable(
                    dragState,
                    Orientation.Horizontal,
                    overscrollEffect = overScrollEffect
                )
                .overscroll(overScrollEffect)
                .offset {
                    IntOffset(
                        x = dragState.requireOffset().roundToInt(),
                        y = 0
                    )
                }
        ) {
            content()
        }
    }
}
