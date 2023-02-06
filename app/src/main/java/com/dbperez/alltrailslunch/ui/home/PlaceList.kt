@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)

package com.dbperez.alltrailslunch.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dbperez.alltrailslunch.R
import com.dbperez.alltrailslunch.domain.model.Place
import com.dbperez.alltrailslunch.ui.theme.LightGreen

@Composable
fun PlaceList(
    modifier: Modifier = Modifier,
    places: List<Place>,
    onPlaceClicked: (place: Place) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(24.dp)
    ) {
        items(places) { place ->
            PlaceListItem(
                place = place,
                onPlaceClicked = { onPlaceClicked(place) }
            )
        }
    }
}

@Composable
fun PlaceListItem(
    modifier: Modifier = Modifier,
    isClickable: Boolean = true,
    place: Place,
    showExtendedDetails: Boolean = false,
    onPlaceClicked: (place: Place) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isClickable) {
                onPlaceClicked(place)
            },
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            GlideImage(
                modifier = Modifier.size(64.dp),
                model = place.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            ) {
                it
                    .placeholder(R.drawable.placeholder_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .load(place.photoUrl)
            }
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = place.name,
                    fontWeight = FontWeight(weight = 700),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        painter = painterResource(id = R.drawable.ic_star),
                        tint = LightGreen,
                        contentDescription = null
                    )
                    Text(
                        text = "${place.rating} • ",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "(${"%,d".format(place.numReviews)})",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                }
                Text(
                    text = place.address,
                    color = Color.Gray,
                    maxLines = if (showExtendedDetails) 4 else 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (showExtendedDetails) {
                    Column {
                        if (place.openNow) {
                            Text(
                                text = stringResource(R.string.open_now),
                                color = Color.Gray,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        place.phoneNumber?.let {
                            Text(
                                text = it,
                                color = Color.Gray,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}