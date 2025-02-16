package com.zaed.common.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun SwipeToEditOrDeleteContainer(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    isEditEnabled: Boolean = false,
    onEdit: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val state = rememberSwipeToDismissBoxState()
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl
    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        state = state,
        backgroundContent = {
            SwipeBackground(
                swipeDismissState = state,
                isEditEnabled = isEditEnabled,
                layoutDirection = layoutDirection
            )
        },
        content = {
            content()
        },
//        enableDismissFromEndToStart = true,
//        enableDismissFromStartToEnd = isEditEnabled

        enableDismissFromEndToStart = if (isRtl) isEditEnabled else true,
        enableDismissFromStartToEnd = if (isRtl) true else isEditEnabled
    )
    when (state.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            LaunchedEffect(key1 = state) {
                if (isRtl) {
                    if (isEditEnabled) onEdit()
                } else {
                    onDelete()
                }
                state.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }
        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(key1 = state) {
                if (isRtl) {
                    onDelete()
                } else if (isEditEnabled) {
                    onEdit()
                }
                state.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }
        else -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBackground(
    modifier: Modifier = Modifier,
    swipeDismissState: SwipeToDismissBoxState,
    isEditEnabled: Boolean,
    layoutDirection: LayoutDirection,
) {
    val isRtl = layoutDirection == LayoutDirection.Rtl

    // Determine background color and icon based on swipe direction
    val (backgroundColor, icon, contentAlignment, iconTint) = when (swipeDismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            if (isRtl) {
                // In RTL, EndToStart is for edit
                if (isEditEnabled) {
                    FourTuple(
                        MaterialTheme.colorScheme.primary,
                        Icons.Default.Edit,
                        Alignment.CenterStart,
                        MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    FourTuple(
                        MaterialTheme.colorScheme.error,
                        Icons.Default.Delete,
                        Alignment.CenterStart,
                        MaterialTheme.colorScheme.onError
                    )
                }
            } else {
                // In LTR, EndToStart is for delete
                FourTuple(
                    MaterialTheme.colorScheme.error,
                    Icons.Default.Delete,
                    Alignment.CenterEnd,
                    MaterialTheme.colorScheme.onError
                )
            }
        }
        SwipeToDismissBoxValue.StartToEnd -> {
            if (isRtl) {
                // In RTL, StartToEnd is for delete
                FourTuple(
                    MaterialTheme.colorScheme.error,
                    Icons.Default.Delete,
                    Alignment.CenterEnd,
                    MaterialTheme.colorScheme.onError
                )
            } else {
                // In LTR, StartToEnd is for edit
                if (isEditEnabled) {
                    FourTuple(
                        MaterialTheme.colorScheme.primary,
                        Icons.Default.Edit,
                        Alignment.CenterStart,
                        MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    FourTuple(
                        Color.Transparent,
                        Icons.Default.Delete,
                        Alignment.CenterStart,
                        MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
        else -> {
            FourTuple(
                Color.Transparent,
                Icons.Default.Delete,
                if (isRtl) Alignment.CenterEnd else Alignment.CenterStart,
                MaterialTheme.colorScheme.onError
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = contentAlignment
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint
        )
    }
}

private data class FourTuple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)