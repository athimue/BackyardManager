package com.athimue.settings.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Text
import com.athimue.backyard.core.theme.AppColors
import com.athimue.backyard.core.theme.AppEffects
import com.athimue.backyard.core.theme.AppTypography
import com.athimue.backyard.core.theme.backyardBackground
import com.athimue.backyard.core.theme.components.BackyardButton
import com.athimue.backyard.core.theme.components.BackyardButtonStyle
import com.athimue.backyard.core.theme.components.BackyardIconButton
import com.athimue.settings.ui.model.SettingsRunnerUiModel
import com.athimue.settings.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onRaceReset: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val resetDone by viewModel.resetDone.collectAsState()

    LaunchedEffect(resetDone) {
        if (resetDone) {
            viewModel.onResetDoneConsumed()
            onRaceReset()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .backyardBackground()
            .padding(horizontal = 40.dp, vertical = 20.dp)
    ) {
        // ── Header ──────────────────────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackyardIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retour",
                onClick = onBack,
            )
            Spacer(Modifier.width(20.dp))
            Text(
                text = "Réglages",
                fontSize = 40.sp,
                fontFamily = AppTypography.titleFontFamily,
                fontWeight = AppTypography.bold,
                color = AppColors.Yellow,
                style = AppEffects.glow(AppColors.Yellow, blurRadius = 24f),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 20.dp)
                .height(2.dp)
                .background(AppColors.Yellow)
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ── Left: start time + race reset ─────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(0.38f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingsCard(
                    title = "Heure de départ",
                    trailing = {
                        Text(
                            text = "appui long : ±10 min",
                            fontSize = AppTypography.labelSize,
                            fontFamily = AppTypography.fontFamily,
                            color = AppColors.Gray,
                        )
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimeStepper(
                            value = uiState.startHour,
                            label = "heures",
                            onUp = viewModel::incrementStartHour,
                            onDown = viewModel::decrementStartHour,
                        )
                        Text(
                            text = ":",
                            fontSize = 52.sp,
                            fontFamily = AppTypography.fontFamily,
                            fontWeight = AppTypography.bold,
                            color = AppColors.Yellow,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 0.dp)
                        )
                        TimeStepper(
                            value = uiState.startMinute,
                            label = "minutes",
                            onUp = { viewModel.incrementStartMinute() },
                            onDown = { viewModel.decrementStartMinute() },
                            onUpLong = { viewModel.incrementStartMinute(10) },
                            onDownLong = { viewModel.decrementStartMinute(10) },
                        )
                    }
                }

                SettingsCard(title = "Remise à zéro", accent = AppColors.RedFilledBorder) {
                    // Both actions wipe lap times → grouped here, double press to confirm
                    ConfirmButton(
                        label = "Restaurer les coureurs",
                        confirmLabel = "Confirmer : efface les temps",
                        icon = Icons.Default.Refresh,
                        style = BackyardButtonStyle.Secondary,
                        onConfirm = viewModel::restoreDefaultRunners,
                    )
                    Spacer(Modifier.height(10.dp))
                    ConfirmButton(
                        label = "Réinitialiser la course",
                        confirmLabel = "Confirmer : tout effacer",
                        icon = Icons.Default.Delete,
                        onConfirm = viewModel::resetRace,
                    )
                }
            }

            // ── Right: runners ────────────────────────────────────────────────
            SettingsCard(
                title = "Coureurs",
                trailing = {
                    Text(
                        text = "${uiState.runners.size} inscrits",
                        fontSize = AppTypography.bodySmallSize,
                        fontFamily = AppTypography.fontFamily,
                        color = AppColors.WhiteDim,
                    )
                },
                modifier = Modifier
                    .weight(0.62f)
                    .fillMaxHeight()
            ) {
                AddRunnerRow(onAdd = viewModel::addRunner)
                Spacer(Modifier.height(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(4.dp),
                ) {
                    items(uiState.runners, key = { it.dossardId }) { runner ->
                        RunnerChip(runner = runner, onRemove = { viewModel.removeRunner(runner.dossardId) })
                    }
                }
            }
        }
    }
}

/** Dark panel with a title, used for each group of settings. */
@Composable
private fun SettingsCard(
    title: String,
    modifier: Modifier = Modifier,
    accent: androidx.compose.ui.graphics.Color = AppColors.Yellow,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.SurfaceDark, RoundedCornerShape(20.dp))
            .border(1.dp, AppColors.GraySubtle, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 24.dp)
                    .background(accent, RoundedCornerShape(2.dp))
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = AppTypography.subtitleSize,
                fontFamily = AppTypography.titleFontFamily,
                color = AppColors.White,
                modifier = Modifier.weight(1f)
            )
            trailing?.invoke()
        }
        Spacer(Modifier.height(14.dp))
        content()
    }
}

/** Big two-digit value with an up arrow above and a down arrow below (D-pad friendly). */
@Composable
private fun TimeStepper(
    value: Int,
    label: String,
    onUp: () -> Unit,
    onDown: () -> Unit,
    onUpLong: (() -> Unit)? = null,
    onDownLong: (() -> Unit)? = null,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BackyardIconButton(
            icon = Icons.Default.KeyboardArrowUp,
            contentDescription = "Augmenter les $label",
            onClick = onUp,
            onLongClick = onUpLong,
            padding = 6.dp,
        )
        Box(
            modifier = Modifier
                .padding(vertical = 6.dp)
                .background(AppColors.Black, RoundedCornerShape(14.dp))
                .border(1.dp, AppColors.Yellow.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                .padding(horizontal = 18.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "%02d".format(value),
                fontSize = 46.sp,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.bold,
                color = AppColors.Yellow,
                style = AppEffects.glow(AppColors.Yellow, blurRadius = 28f),
            )
        }
        BackyardIconButton(
            icon = Icons.Default.KeyboardArrowDown,
            contentDescription = "Diminuer les $label",
            onClick = onDown,
            onLongClick = onDownLong,
            padding = 6.dp,
        )
        Text(
            text = label,
            fontSize = AppTypography.labelSize,
            fontFamily = AppTypography.fontFamily,
            color = AppColors.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

/** Runner line: bib badge, first name, remove (double press). */
@Composable
private fun RunnerChip(runner: SettingsRunnerUiModel, onRemove: () -> Unit) {
    var armed by remember { mutableStateOf(false) }
    LaunchedEffect(armed) {
        if (armed) {
            delay(CONFIRM_WINDOW_MS)
            armed = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (armed) AppColors.RedFilled else AppColors.SurfaceCell,
                RoundedCornerShape(14.dp)
            )
            .padding(start = 10.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(AppColors.Yellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = runner.dossardId.toString(),
                fontSize = AppTypography.labelSize,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.bold,
                color = AppColors.Black,
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = if (armed) "Retirer ${runner.firstName} ?" else runner.firstName,
            fontSize = AppTypography.bodySmallSize,
            fontFamily = AppTypography.fontFamily,
            fontWeight = AppTypography.semiBold,
            color = AppColors.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        BackyardIconButton(
            icon = Icons.Default.Close,
            contentDescription = "Retirer ${runner.firstName}",
            style = BackyardButtonStyle.Danger,
            iconSize = 18.dp,
            padding = 8.dp,
            onClick = {
                if (armed) {
                    armed = false
                    onRemove()
                } else {
                    armed = true
                }
            },
            modifier = Modifier.onFocusChanged { if (!it.hasFocus) armed = false },
        )
    }
}

/**
 * Destructive action guarded by a second press: the first press arms the button
 * (label changes) and the second one within [CONFIRM_WINDOW_MS] runs [onConfirm].
 * Avoids losing a whole race to a single stray OK press on the remote.
 */
@Composable
private fun ConfirmButton(
    label: String,
    confirmLabel: String,
    icon: ImageVector,
    onConfirm: () -> Unit,
    style: BackyardButtonStyle = BackyardButtonStyle.Danger,
    fontSize: TextUnit = AppTypography.bodySmallSize,
) {
    var armed by remember { mutableStateOf(false) }

    LaunchedEffect(armed) {
        if (armed) {
            delay(CONFIRM_WINDOW_MS)
            armed = false
        }
    }

    BackyardButton(
        text = if (armed) confirmLabel else label,
        icon = if (armed) Icons.Default.Warning else icon,
        // Once armed, always show it in red, whatever the resting style
        style = if (armed) BackyardButtonStyle.Danger else style,
        armed = armed,
        fontSize = fontSize,
        onClick = {
            if (armed) {
                armed = false
                onConfirm()
            } else {
                armed = true
            }
        },
        modifier = Modifier.onFocusChanged { if (!it.hasFocus) armed = false },
    )
}

private const val CONFIRM_WINDOW_MS = 3_000L

/** Text field (opens the TV keyboard on OK) + button to add a runner during setup. */
@Composable
private fun AddRunnerRow(onAdd: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var fieldFocused by remember { mutableStateOf(false) }
    val submit = {
        if (name.isNotBlank()) {
            onAdd(name)
            name = ""
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BasicTextField(
            value = name,
            onValueChange = { name = it.take(MAX_NAME_LENGTH) },
            singleLine = true,
            textStyle = TextStyle(
                color = AppColors.White,
                fontSize = AppTypography.bodySmallSize,
                fontFamily = AppTypography.fontFamily
            ),
            cursorBrush = SolidColor(AppColors.Yellow),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { submit() }),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { fieldFocused = it.isFocused }
                .background(AppColors.Black, RoundedCornerShape(14.dp))
                .border(
                    width = if (fieldFocused) 2.dp else 1.dp,
                    color = if (fieldFocused) AppColors.Yellow else AppColors.GraySubtle,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 18.dp, vertical = 14.dp),
            decorationBox = { inner ->
                Box {
                    if (name.isEmpty()) {
                        Text(
                            text = "Prénom du nouveau coureur",
                            color = AppColors.Gray,
                            fontSize = AppTypography.bodySmallSize,
                            fontFamily = AppTypography.fontFamily
                        )
                    }
                    inner()
                }
            }
        )
        BackyardButton(
            text = "Ajouter",
            icon = Icons.Default.Add,
            style = BackyardButtonStyle.Primary,
            onClick = submit,
        )
    }
}

private const val MAX_NAME_LENGTH = 20
