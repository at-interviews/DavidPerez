package com.dbperez.alltrailslunch.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dbperez.alltrailslunch.domain.model.Place

@Composable
fun PlaceDetails(
    modifier: Modifier = Modifier,
    place: Place,
    onNavBackFromPlaceDetails: () -> Unit
) {
    BackHandler(enabled = true) {
        onNavBackFromPlaceDetails()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        PlaceListItem(
            isClickable = false,
            place = place,
            showExtendedDetails = true,
            onPlaceClicked = { /* do nothing */ }
        )
    }
}