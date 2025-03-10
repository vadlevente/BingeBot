package com.vadlevente.bingebot.settings.ui.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.core.model.SelectedLanguage
import com.vadlevente.bingebot.settings.R
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.ui.bottomSheetBottomPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLanguageBottomSheet(
    languages: Map<SelectedLanguage, Boolean>,
    onSelectLanguage: (SelectedLanguage) -> Unit,
    onDismiss: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                text = stringResource(id = R.string.settings_selectLanguageTitle),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(languages.keys.toList()) { index, language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (languages[language] == false) {
                                    onSelectLanguage(language)
                                    onDismiss()
                                }
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 24.dp)
                                .padding(horizontal = 16.dp),
                            text = language.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        if (languages[language] == true) {
                            Icon(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                imageVector = Filled.Done,
                                contentDescription = null,
                                tint = BingeBotTheme.colors.highlight,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(bottomSheetBottomPadding))
        }
    }

    BackHandler {
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
    }
}
