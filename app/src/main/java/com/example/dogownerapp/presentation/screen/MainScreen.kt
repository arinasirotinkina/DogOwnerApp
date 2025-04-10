import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.dogownerapp.presentation.viewmodel.EditDogViewModel
import com.example.dogownerapp.presentation.viewmodel.HealthViewModel
import com.example.dogownerapp.presentation.screen.health.EditDog
import com.example.dogownerapp.presentation.screen.home.EditProfile
import com.example.dogownerapp.presentation.screen.health.Health
import com.example.dogownerapp.presentation.screen.home.Home
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.screen.care.Care
import com.example.dogownerapp.presentation.screen.care.ChatListScreen
import com.example.dogownerapp.presentation.screen.care.ChatScreen
import com.example.dogownerapp.presentation.screen.care.ReadRec
import com.example.dogownerapp.presentation.screen.care.ReadSpecProfile
import com.example.dogownerapp.presentation.screen.care.Recommends
import com.example.dogownerapp.presentation.screen.care.SearchSpec
import com.example.dogownerapp.presentation.screen.specialist.SpecialistVersion
import com.example.dogownerapp.presentation.ui.CustomTheme
import com.example.dogownerapp.presentation.viewmodel.ChatViewModel
import com.example.dogownerapp.presentation.viewmodel.PlansViewModel
import com.example.dogownerapp.presentation.viewmodel.RecommendsViewModel
import com.example.dogownerapp.presentation.viewmodel.SpecsViewModel
import com.example.dogownerapp.presentation.viewmodel.UserViewModel
import com.example.dogownerapp.presentation.viewmodel.specialists.ProfileViewModel
import kotlinx.coroutines.delay

@Composable
fun SubMain(healthViewModel: HealthViewModel, editDogViewModel: EditDogViewModel,
            userViewModel: UserViewModel, plansViewModel: PlansViewModel,
            chatViewModel: ChatViewModel, recsViewModel: RecommendsViewModel,
            profileViewModel: ProfileViewModel, authViewModel: AuthViewModel,
            specsViewModel: SpecsViewModel
) {

    var loading by remember { mutableStateOf(false) }
    val user by userViewModel.user.collectAsState()
    LaunchedEffect(user) {
        loading = true
    }

    if (loading) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.start_image),
                contentDescription = "Start Image",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

    }
    if (user.role == "owner") {
        Main(healthViewModel, editDogViewModel,
            userViewModel, plansViewModel,
            chatViewModel, recsViewModel,
            specsViewModel, authViewModel)
    }
    else if (user.role == "specialist") {
        SpecialistVersion(authViewModel, profileViewModel, chatViewModel)
    }
}


@Composable
fun Main(healthViewModel: HealthViewModel,
         editDogViewModel: EditDogViewModel,
         userViewModel: UserViewModel,
         plansViewModel: PlansViewModel,
         chatViewModel: ChatViewModel,
         recsViewModel: RecommendsViewModel,
         specsViewModel: SpecsViewModel,
         authViewModel: AuthViewModel
) {
    CustomTheme {
        val navController = rememberNavController()
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val bottomBarRoutes = listOf("home", "health", "plans", "care")
        Scaffold(
            bottomBar = {
                if (currentRoute in bottomBarRoutes) {
                    BottomNavigationBar(navController)
                }
            }
        ) { innerPadding ->
            Column(Modifier.padding(8.dp)) {
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.Health.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("health_back") { Health(healthViewModel, navController) }
                    composable(NavRoutes.Health.route) { Health(healthViewModel, navController) }
                    composable(NavRoutes.Planning.route) { Planning(plansViewModel)  }
                    composable(NavRoutes.Care.route) { Care(navController) }

                    composable(NavRoutes.ChatList.route) { ChatListScreen(chatViewModel,
                        navController, true) }

                    composable(NavRoutes.Home.route) { Home(userViewModel, authViewModel, navController) }
                    composable(NavRoutes.Recs.route) { Recommends(recsViewModel, navController) }
                    composable(NavRoutes.Vets.route) { SearchSpec(specsViewModel, "Ветеринар",
                        navController, userViewModel)}
                    composable(NavRoutes.Dogsitters.route) { SearchSpec(specsViewModel, "Догситтер",
                        navController, userViewModel)}
                    composable(NavRoutes.Groomers.route) { SearchSpec(specsViewModel, "Грумер",
                        navController, userViewModel)}

                    composable(NavRoutes.Favourites.route) { SearchSpec(specsViewModel, "Избранное",
                        navController, userViewModel)}


                    composable(
                        route = "edit_dog/{dogId}?",
                        arguments = listOf(navArgument("dogId") {
                            nullable = true
                            defaultValue = null
                        })
                    ) { backStackEntry ->
                        val dogId = backStackEntry.arguments?.getString("dogId")
                        EditDog(editDogViewModel, healthViewModel, navController, dogId)
                    }
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
                        ChatScreen(chatViewModel, chatId, name, true)
                    }

                    composable(
                        route = "read_rec/{recId}?",
                        arguments = listOf(navArgument("recId") {
                            nullable = false
                        })
                    ) { backStackEntry ->
                        val recId = backStackEntry.arguments?.getString("recId")
                        ReadRec(recsViewModel, recId!!)
                    }
                    composable(
                        route = "read_spec/{specId}?",
                        arguments = listOf(navArgument("specId") {
                            nullable = false
                        })
                    ) { backStackEntry ->
                        val specId = backStackEntry.arguments?.getString("specId")
                        ReadSpecProfile(specsViewModel, navController, specId!!, userViewModel)
                    }
                    composable(NavRoutes.EditDog.route) { EditDog(editDogViewModel, healthViewModel,
                        navController, null) }
                    composable(NavRoutes.EditProfile.route) { EditProfile(userViewModel, navController) }

                }

            }

        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    CustomTheme() {

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surface,

        ) {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            var barItems = NavBarItems()
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
fun NavBarItems() : List<BarItem>{
    return listOf(
        BarItem(
            title = "Собаки",
            image =  (R.drawable.doglist_icon),
            route = "health"
        ),
        BarItem(
            title = "Планы",
            image =  (R.drawable.plans_icon),
            route = "plans"
        ),
        BarItem(
            title = "Уход",
            image = (R.drawable.care_icon),
            route = "care"
        ),
        BarItem(
            title = "Профиль",
            image = (R.drawable.home_icon),
            route = "home"
        )

    )
}

data class BarItem(
    val title: String,
    val image: Int,
    val route: String
)


sealed class NavRoutes(val route: String) {
    data object Health : NavRoutes("health")
    data object Planning : NavRoutes("plans")
    data object Care : NavRoutes("care")
    data object Home : NavRoutes("home")
    data object EditDog : NavRoutes("edit_dog")
    data object EditProfile : NavRoutes("edit_profile")
    data object ChatList : NavRoutes("chat_list")
    data object Recs : NavRoutes("recs")
    data object Vets: NavRoutes("vets")
    data object Groomers: NavRoutes("groomers")
    data object Dogsitters: NavRoutes("dogsitters")
    data object Favourites: NavRoutes("favourites")
}