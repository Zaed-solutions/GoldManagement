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
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = isEditEnabled
    )
    when (state.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            LaunchedEffect(key1 = state) {
                onDelete()
                state.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(key1 = state) {
                if (isEditEnabled) {
                    onEdit()
                    state.snapTo(SwipeToDismissBoxValue.Settled)
                }
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
    val (backgroundColor, icon, contentAlignment) = when (swipeDismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            Triple(
                MaterialTheme.colorScheme.error, // Red for delete
                Icons.Default.Delete,
                if (isRtl) Alignment.CenterStart else Alignment.CenterEnd
            )
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            if(isEditEnabled) {
                Triple(
                    MaterialTheme.colorScheme.primary, // Blue for edit
                    Icons.Default.Edit,
                    if (isRtl) Alignment.CenterEnd else Alignment.CenterStart
                )
            } else {
                Triple(
                    Color.Transparent,
                    Icons.Default.Delete,
                    Alignment.CenterEnd
                )
            }
        }

        else -> {
            Triple(
                Color.Transparent,
                Icons.Default.Delete,
                Alignment.CenterEnd
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
            tint = if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                MaterialTheme.colorScheme.onError
            } else {
                MaterialTheme.colorScheme.onPrimary
            }
        )
    }
}