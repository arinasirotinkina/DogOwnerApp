package com.example.dogownerapp.presentation.screen.specialist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dogownerapp.R
import com.example.dogownerapp.presentation.auth.AuthViewModel
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.screen.care.ChatListScreen
import com.example.dogownerapp.presentation.screen.care.ChatScreen
import com.example.dogownerapp.presentation.ui.CustomTheme
import com.example.dogownerapp.presentation.viewmodel.ChatViewModel
import com.example.dogownerapp.presentation.viewmodel.specialists.ProfileViewModel

@Composable
fun SpecialistVersion(authViewModel: AuthViewModel, profileViewModel: ProfileViewModel, chatViewModel: ChatViewModel) {
    CustomTheme {
        val navController = rememberNavController()
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val bottomBarRoutes = listOf("profile", "chats")

        Scaffold(
            bottomBar = {
                if (currentRoute in bottomBarRoutes) {
                    BottomNavigationBarSpec(navController)
                }
            }
        ) { innerPadding ->
            Column(Modifier.padding(8.dp)) {
                NavHost(
                    navController = navController,
                    startDestination = "profile",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("profile") { Profile(authViewModel, profileViewModel, navController) }
                    composable("edit_profile") { EditProfileScreen(profileViewModel, navController) }
                    composable("chats") { ChatListScreen(chatViewModel,
                        navController, false) }
                    composable(
                        route = "chat/{chatId}/name={name}",
                        arguments = listOf(navArgument("chatId") {
                            nullable = false
                        }, navArgument("name") {
                            nullable = false
                        })
                    ) { backStackEntry ->
                        val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        ChatScreen(chatViewModel, navController, chatId, name, false)
                    }
                }

            }
        }
    }

}

@Composable
fun BottomNavigationBarSpec(navController: NavController) {
    CustomTheme() {

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surface,

            ) {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            var barItems = navBarItems()
            barItems.forEach { navItem ->
                NavigationBarItem(
                    selected = currentRoute == navItem.route,
                    onClick = {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = navItem.image),
                            contentDescription = "Dog",
                            modifier = Modifier.size(48.dp)
                        )
                    },
                    label = {
                        Text(
                            text = navItem.title,
                            fontSize = 16.sp
                        )
                    },

                    colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                        selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                        indicatorColor = customColors.onSurface
                    )
                )
            }
        }
    }
}

@Composable
fun navBarItems() : List<BarItem>{
    return listOf(
        BarItem(
            title = "Профиль",
            image =  (R.drawable.doglist_icon),
            route = "profile"
        ),
        BarItem(
            title = "Чаты",
            image =  (R.drawable.plans_icon),
            route = "chats"
        )
    )
}

data class BarItem(
    val title: String,
    val image: Int,
    val route: String
)


