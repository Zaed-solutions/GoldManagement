package com.zaed.common.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QuestionMark
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
    isDeleteEnabled: Boolean = true,
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
                isDeleteEnabled = isDeleteEnabled,
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
                onDelete()
                state.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(key1 = state) {
                onEdit()
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
    isDeleteEnabled: Boolean,
    isEditEnabled: Boolean,
    layoutDirection: LayoutDirection,
) {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val (backgroundColor, icon, contentAlignment, iconTint) = when (swipeDismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            if (isRtl) {
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
            } else if(isDeleteEnabled) {
                FourTuple(
                    MaterialTheme.colorScheme.error,
                    Icons.Default.Delete,
                    Alignment.CenterEnd,
                    MaterialTheme.colorScheme.onError
                )
            } else {
                FourTuple(
                    Color.Transparent,
                    Icons.Default.QuestionMark,
                    Alignment.CenterEnd,
                    MaterialTheme.colorScheme.onSurface
                )
            }
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            if (isRtl) {
                FourTuple(
                    MaterialTheme.colorScheme.error,
                    Icons.Default.Delete,
                    Alignment.CenterEnd,
                    MaterialTheme.colorScheme.onError
                )
            } else {
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

data class FourTuple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)