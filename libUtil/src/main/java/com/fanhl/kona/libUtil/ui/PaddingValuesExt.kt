package com.fanhl.kona.libUtil.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    return PaddingValues(
        start = calculateStartPadding(LocalLayoutDirection.current) + other.calculateStartPadding(LocalLayoutDirection.current),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(LocalLayoutDirection.current) + other.calculateEndPadding(LocalLayoutDirection.current),
        bottom = calculateBottomPadding() + other.calculateBottomPadding()
    )
}

@Composable
operator fun PaddingValues.plus(dp: Dp): PaddingValues {
    return PaddingValues(
        start = calculateStartPadding(LocalLayoutDirection.current) + dp,
        top = calculateTopPadding() + dp,
        end = calculateEndPadding(LocalLayoutDirection.current) + dp,
        bottom = calculateBottomPadding() + dp
    )
} 