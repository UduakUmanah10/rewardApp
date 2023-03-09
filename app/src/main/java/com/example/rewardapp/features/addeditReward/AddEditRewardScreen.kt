package com.example.rewardapp.features.addeditReward

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rewardapp.R
import com.example.rewardapp.core.ui.composables.ITiconButton
import com.example.rewardapp.core.ui.theme.RewardAppTheme
import com.example.rewardapp.database.IconKeysEn
import com.example.rewardapp.database.defaulticon
import com.google.accompanist.flowlayout.FlowRow
interface AddEditRewardScreenActions {
    fun onRewardIconSelected(reward: IconKeysEn)
    fun onRewardIconButtonClicked()
    fun cancelRewardIconDialouge()
    fun onRewardInputChanged(input: String)
    fun onRewardInPercentChanged(input: Int)
    fun onSavedClicked()
}

@Composable
fun AddEditRewardScreen(navController: NavController, value: Int?) {
    val viewModel: AddEditViewModel = hiltViewModel()
    val rewardNameIsInputIsError by viewModel.rewardNameInputIsError.observeAsState(false)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AddEditViewModel.AddEditRewardEvent.RewardCreated -> navController.popBackStack()
                else -> {}
            }
        }
    }

    val showRewardIconSelectionDialogRewardEvent by viewModel
        .showRewardIconSelectionDialogRewardEvent.observeAsState()

    val rewardInput by viewModel.rewardNameInput.observeAsState("")
    val isEditModel = viewModel.isEditMode
    val ChancesInPercentInput by viewModel.ChancesInPercent.observeAsState(10)

    val rewardIconkey by viewModel.showRewardIconSelectionDialogRewardEvent.observeAsState(
        defaulticon
    )
    val DefaultRewardIcokKey by viewModel.DefaultRewardIconKey.observeAsState(defaulticon)

    ScreenContent(
        rewardIconKeySelected = DefaultRewardIcokKey,
        isEditMode = isEditModel,
        rewardNameInput = rewardInput,
        ChancesInPercent = ChancesInPercentInput,
        showRewardIconSelectionDialog = showRewardIconSelectionDialogRewardEvent,
        actions = viewModel,
        rewardNameInputError = rewardNameIsInputIsError,
        onClosedClicked = { navController.popBackStack() }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenContent(
    rewardNameInputError: Boolean,
    rewardIconKeySelected: IconKeysEn,
    isEditMode: Boolean,
    rewardNameInput: String,
    ChancesInPercent: Int,
    showRewardIconSelectionDialog: Boolean?,
    actions: AddEditRewardScreenActions,
    onClosedClicked: () -> Unit

) {
    Scaffold(

        topBar = {
            val appBarTitle = stringResource(if (isEditMode) R.string.EditReward else R.string.add_new_reward)
            TopAppBar(
                title = { Text(appBarTitle) },

                navigationIcon = {
                    IconButton(onClick = onClosedClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = actions::onSavedClicked) {
                Icon(Icons.Default.Check, contentDescription = stringResource(id = R.string.add_new_reward))
            }
        }

    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = rewardNameInput,
                onValueChange = actions::onRewardInputChanged,
                label = { Text(stringResource(id = R.string.reward_Name)) },
                modifier = Modifier.fillMaxWidth(),
                isError = rewardNameInputError
            )
            if (rewardNameInputError) {
                Text(
                    text = stringResource(R.string.fiele_cant_be_blank),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.error
                )
            }
            Spacer(modifier = Modifier.height(35.dp))
            Slider(
                value = ChancesInPercent.toFloat() / 100,
                onValueChange = { chancesAsFloat ->
                    actions.onRewardInPercentChanged((chancesAsFloat * 100).toInt())
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            val iconButtonBackground = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray
            ITiconButton(
                onClick = actions::onRewardIconButtonClicked,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = rewardIconKeySelected.reward,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
    if (showRewardIconSelectionDialog!!) {
        RewardIconsSelectionDialog(onDismissRequest = actions::cancelRewardIconDialouge, onIconSelected = actions::onRewardIconSelected)
    }
}

@Composable
private fun RewardIconsSelectionDialog(
    onDismissRequest: () -> Unit,
    onIconSelected: (iconKey: IconKeysEn) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.cancel))
            }
        },

        text = {
            FlowRow {
                IconKeysEn.values().forEach {
                    IconButton(onClick = {
                        onIconSelected(it)
                        onDismissRequest()
                    }) {
                        Icon(imageVector = it.reward, contentDescription = null)
                    }
                }
            }
        }

    )
}

@Preview(
    name = " eward item Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = " Reward item Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
private fun ScreenContentPreview() {
    RewardAppTheme {
        Surface {
            ScreenContent(
                isEditMode = true,
                rewardNameInput = "",
                ChancesInPercent = 10,
                onClosedClicked = {},
                showRewardIconSelectionDialog = false,
                rewardNameInputError = false,
                rewardIconKeySelected = defaulticon,
                actions = object : AddEditRewardScreenActions {
                    override fun onRewardIconSelected(reward: IconKeysEn) {}
                    override fun onRewardIconButtonClicked() {}
                    override fun cancelRewardIconDialouge() {}
                    override fun onRewardInputChanged(input: String) {}
                    override fun onRewardInPercentChanged(input: Int) {}
                    override fun onSavedClicked() {}
                }

            )
        }
    }
}
val listPadding: Dp = 64.dp
