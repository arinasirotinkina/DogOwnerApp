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
import com.example.dogownerapp.presentation.viewmodel.EditDogViewModel
import com.example.dogownerapp.presentation.viewmodel.HealthViewModel
import com.example.dogownerapp.presentation.screen.health.EditDog
import com.example.dogownerapp.presentation.screen.home.EditProfile
import com.example.dogownerapp.presentation.screen.health.Health
import com.example.dogownerapp.presentation.screen.home.Home
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.screen.care.Care
import com.example.dogownerapp.presentation.ui.CustomTheme
import com.example.dogownerapp.presentation.viewmodel.PlansViewModel
import com.example.dogownerapp.presentation.viewmodel.UserViewModel


@Composable
fun Main(healthViewModel: HealthViewModel,
         editDogViewModel: EditDogViewModel,
         userViewModel: UserViewModel,
         plansViewModel: PlansViewModel
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
                    startDestination = NavRoutes.Home.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(NavRoutes.Health.route) { Health(healthViewModel, navController) }
                    composable(NavRoutes.Planning.route) { Planning(plansViewModel)  }
                    composable(NavRoutes.Care.route) { Care() }
                    composable(NavRoutes.Home.route) { Home(userViewModel, navController) }
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
                    composable(NavRoutes.EditDog.route) { EditDog(editDogViewModel, healthViewModel, navController, null) }
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
                        selectedTextColor = MaterialTheme.colorScheme.onSecondary, // Цвет текста в активном состоянии
                        indicatorColor = customColors.onSurface // Цвет фона активного элемента
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
            title = "Собака",
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
    object Health : NavRoutes("health")
    object Planning : NavRoutes("plans")
    object Care : NavRoutes("care")
    object Home : NavRoutes("home")
    object EditDog : NavRoutes("edit_dog")
    object UpdateDog : NavRoutes("update_dog")
    object EditProfile : NavRoutes("edit_profile")
}
/* val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavVisible = currentRoute !in listOf("edit_dog", "some_other_screen")

    Scaffold(
        bottomBar = {
            if (bottomNavVisible) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("edit_dog") { EditDogScreen(navController) } // Здесь скрываем меню
            composable("some_other_screen") { SomeOtherScreen(navController) }
        }
    }*/