package com.example.rewardapp

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rewardapp.core.ui.theme.RewardAppTheme
import com.example.rewardapp.features.addeditReward.AddEditRewardScreen
import com.example.rewardapp.features.rewardList.RewardListScreen
import com.example.rewardapp.features.timer.TimerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RewardAppTheme {
                ScreenContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScreenContent() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavDestinatiom.forEach { Destination ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                Destination.icon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(stringResource(id = Destination.label))
                        },
                        alwaysShowLabel = true,

                        selected = currentDestination?.hierarchy?.any { it.route == Destination.route } == true,

                        onClick = {
                            navController.navigate(Destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        }
                    )
                }
            }
        }

    ) { innerpadding ->
        NavHost(
            navController = navController,
            startDestination = bottomNavDestinatiom[0].route,
            Modifier.padding(innerpadding)
        ) {
            composable(BottomNavDestination.Timer.route) {
                TimerScreen()
            }
            composable(BottomNavDestination.RewardList.route) {
                RewardListScreen(navController)
            }
            composable(
                route = FullScreenDestination.AddEdiRewardScreen.route + "/{name}",
                arguments = listOf(

                    navArgument("name") {
                        type = NavType.IntType
                        defaultValue = -1
                        nullable = false
                    }
                )

            ) { entry ->
                AddEditRewardScreen(navController = navController, value = entry.arguments?.getInt("name"))
            }
        }
    }
}

val bottomNavDestinatiom = listOf<BottomNavDestination>(
    BottomNavDestination.Timer,
    BottomNavDestination.RewardList
)

sealed class BottomNavDestination(
    val route: String,
    @StringRes val label: Int,
    val icon: ImageVector
) {
    object Timer : BottomNavDestination("timer", R.string.timer, Icons.Outlined.AccountBox)
    object RewardList : BottomNavDestination("rewardList", R.string.reward_list, Icons.Outlined.List)

    fun passParameter(vararg input: Long): String {
        return buildString {
            append(route)
            input.forEach { arg ->
                append("/$arg")
            }
        }
    }
}

sealed class FullScreenDestination(
    val route: String
) {
    object AddEdiRewardScreen : FullScreenDestination(route = "add_edit_reward")
    fun passParameter(vararg input: Long): String {
        return buildString {
            append(route)
            input.forEach { arg ->
                append("/$arg")
            }
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun DefaultPreview() {
    RewardAppTheme {
        Surface() {
            ScreenContent()
        }
    }
}
