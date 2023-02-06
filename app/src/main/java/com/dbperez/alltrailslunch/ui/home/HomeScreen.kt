@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)

package com.dbperez.alltrailslunch.ui.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.dbperez.alltrailslunch.R
import com.dbperez.alltrailslunch.domain.model.Place
import com.dbperez.alltrailslunch.ui.theme.AllTrailsDarkGreen
import com.dbperez.alltrailslunch.ui.theme.AllTrailsGray
import com.dbperez.alltrailslunch.ui.theme.AllTrailsGreen
import com.google.android.gms.maps.model.Marker

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        ) {
            viewModel.getNearbyPlaces()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            MapListToggleFab(state) {
                viewModel.toggleMapListView()
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { contentPadding ->
        HomeContent(
            contentPadding = contentPadding,
            state = state,
            onMapMarkerClicked = { viewModel.mapMarkerClicked(it) },
            onMapInfoWindowClosed = { viewModel.onMapInfoWindowClosed() },
            onSearchSubmitted = { viewModel.onSearchSubmitted(it) },
            onPlaceClicked = { viewModel.onPlaceClicked(it) },
            onNavBackFromPlaceDetails = { viewModel.getNearbyPlaces() }
        )
    }
}

@Composable
private fun HomeContent(
    contentPadding: PaddingValues,
    state: HomeScreenUiState,
    onMapMarkerClicked: (marker: Marker) -> Unit,
    onMapInfoWindowClosed: () -> Unit,
    onSearchSubmitted: (searchInput: String) -> Unit,
    onPlaceClicked: (place: Place) -> Unit,
    onNavBackFromPlaceDetails: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchHeader(
            onSearchSubmitted = { onSearchSubmitted(it) }
        )

        AnimatedContent(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(color = AllTrailsGray),
            targetState = state,
            transitionSpec = { fadeIn(animationSpec = tween(delayMillis = 100)) with fadeOut() }
        ) { targetState ->
            when (targetState) {
                HomeScreenUiState.Loading -> Loading()
                is HomeScreenUiState.Error -> ErrorMessage(targetState.message)
                is HomeScreenUiState.PlaceList -> PlaceList(
                    places = targetState.places,
                    onPlaceClicked = { onPlaceClicked(it) }
                )
                is HomeScreenUiState.PlaceMap -> PlaceMap(
                    places = targetState.places,
                    currentLocation = targetState.currentLocation,
                    onMapMarkerClicked = {
                        onMapMarkerClicked(it)
                    },
                    onMapInfoWindowClosed = {
                        onMapInfoWindowClosed()
                    },
                    onPlaceClicked = { onPlaceClicked(it) }
                )
                is HomeScreenUiState.PlaceDetails -> {
                    PlaceDetails(
                        place = targetState.place,
                        onNavBackFromPlaceDetails = {
                            onNavBackFromPlaceDetails()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchHeader(
    onSearchSubmitted: (searchInput: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AllTrailsTitle()
        SearchField(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
            onSearchSubmitted = { onSearchSubmitted(it) }
        )
        Divider(color = Color.LightGray)
    }
}

@Composable
private fun AllTrailsTitle() {
    val allTrailsPrefix = stringResource(R.string.alltrails_title_prefix)
    val title =
        stringResource(R.string.alltrails_title_prefix) + stringResource(id = R.string.alltrails_title_suffix)
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 4.dp),
            painter = painterResource(id = R.drawable.logo),
            tint = AllTrailsDarkGreen,
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            text = AnnotatedString(
                text = title,
                spanStyles = listOf(
                    AnnotatedString.Range(
                        item = SpanStyle(
                            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                            color = AllTrailsDarkGreen
                        ),
                        start = 0,
                        end = allTrailsPrefix.length
                    ),
                    AnnotatedString.Range(
                        item = SpanStyle(
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                            color = Color.Gray
                        ),
                        start = allTrailsPrefix.length,
                        end = title.length
                    )
                ),
            )
        )
    }
}

@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    onSearchSubmitted: (searchInput: String) -> Unit
) {
    var searchInput by remember { mutableStateOf("") }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp)),
        value = searchInput,
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        label = {
            Text(
                text = stringResource(R.string.search_restaurants),
                color = Color.Gray
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.DarkGray,
            containerColor = AllTrailsGray,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        onValueChange = {
            searchInput = it
        },
        keyboardActions = KeyboardActions(
            onDone = {
                onSearchSubmitted(searchInput)
            }
        )
    )
}


@Composable
private fun MapListToggleFab(
    state: HomeScreenUiState,
    onMapListToggleClick: () -> Unit
) {
    val fabText =
        if (state is HomeScreenUiState.PlaceMap ||
            state is HomeScreenUiState.PlaceDetails
        ) {
            stringResource(R.string.list_toggle_text)
        } else {
            stringResource(R.string.map_toggle_text)
        }

    val fabIcon =
        if (state is HomeScreenUiState.PlaceMap ||
            state is HomeScreenUiState.PlaceDetails
        ) {
            painterResource(id = R.drawable.ic_list)
        } else {
            painterResource(id = R.drawable.ic_map)
        }

    ExtendedFloatingActionButton(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(corner = CornerSize(48.dp)))
            .width(117.dp),
        text = {
            Text(
                text = fabText,
                color = Color.White
            )
        },
        icon = {
            Icon(
                painter = fabIcon,
                contentDescription = null,
                tint = Color.White
            )
        },
        containerColor = AllTrailsGreen,
        onClick = { onMapListToggleClick() },
    )
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = AllTrailsGreen)
    }
}

@Composable
private fun ErrorMessage(message: String) {
    val errorMessage = message.ifEmpty { stringResource(id = R.string.generic_location_error) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = errorMessage)
    }
}