
package com.example.rewardapp.features.rewardList
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rewardapp.FullScreenDestination
import com.example.rewardapp.R
import com.example.rewardapp.core.ui.theme.RewardAppTheme
import com.example.rewardapp.database.*
import com.example.rewardapp.features.addeditReward.ARG_REWARD_ID
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun RewardListScreen(
    navController: NavController,
    viewModel: rewardListViewModel = hiltViewModel()
) {
    val Rewards by viewModel.rewards.observeAsState(listOf())
    ScreenContent(
        Rewards,
        onAddNewRewardClicked =
        { navController.navigate(FullScreenDestination.AddEdiRewardScreen.route) },
        onRewardItemClicked = { id ->
            navController.navigate(FullScreenDestination.AddEdiRewardScreen.passParameter(id))
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun ScreenContent(
    rewards: List<RewardEntity>,
    onAddNewRewardClicked: () -> Unit,
    onRewardItemClicked: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(stringResource(R.string.reward_screen))
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewRewardClicked) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_new_reward))
//
            }
        }
    ) { paddingValue ->

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 8.dp,
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                ),
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(rewards) { reward ->
                    RewardItem(reward = reward, onItemClicked = { id ->
                        onRewardItemClicked(id)
                    })
                }
            }
            if (listState.firstVisibleItemIndex > 5) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Icon(Icons.Outlined.ArrowDropDown, contentDescription = stringResource(R.string.dropdown))
//
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun RewardItem(
    onItemClicked: (Long) -> Unit,
    reward: RewardEntity,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onItemClicked(reward.id) },
        modifier = modifier
            .fillMaxWidth()
            .padding()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = IconKeysEn.valueOf(reward.IconKey).reward,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(10.dp)
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(reward.title, fontWeight = FontWeight.Bold)
                Text("${reward.ChancesInPercent}%", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview(
    name = " Reward item Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = " Reward item Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@ExperimentalMaterialApi
@Composable
private fun RewardItemScreen() {
    val reward = RewardEntity(IconKeys.CAKE, "this", 6)
    RewardAppTheme {
        Surface {
            RewardItem(reward = reward, onItemClicked = {})
        }
    }
}

@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
@ExperimentalMaterialApi
private fun PreviewScreen() {
    RewardAppTheme {
        Surface() {
            ScreenContent(
                listOf(
                    RewardEntity(IconKeys.CAKE, title = "Reward1", 5)
                ),
                onAddNewRewardClicked = {},
                onRewardItemClicked = {}
            )
//
        }
    }
}
