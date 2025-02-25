import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dogownerapp.R
import com.example.dogownerapp.data.datasource.FirebaseAuthDataSource
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.viewmodel.EditDogViewModel
import com.example.dogownerapp.presentation.viewmodel.HealthViewModel
import com.example.dogownerapp.presentation.screen.EditDog
import com.example.dogownerapp.presentation.screen.EditProfile
import com.example.dogownerapp.presentation.screen.Health
import com.example.dogownerapp.presentation.screen.Home
import com.example.dogownerapp.presentation.screen.customColors
import com.example.dogownerapp.presentation.ui.CustomTheme
import com.example.dogownerapp.presentation.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Date


@Composable
fun Main(healthViewModel: HealthViewModel,
         editDogViewModel: EditDogViewModel,
         userViewModel: UserViewModel
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
                    composable(NavRoutes.Planning.route) { Planning()  }
                    composable(NavRoutes.Care.route) { Care()  }
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
                /*NavHost(navController, startDestination = NavRoutes.Home.route, modifier = Modifier.weight(1f)) {
                    composable(NavRoutes.Health.route) { Health(healthViewModel, navController) }
                    composable(NavRoutes.Planning.route) { Planning()  }
                    composable(NavRoutes.Care.route) { Care()  }
                    composable(NavRoutes.Home.route) { Home(userViewModel) }
                    //composable(NavRoutes.UpdateDog.route) { Home() }
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
                }
                BottomNavigationBar(navController = navController)*/
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
            title = "Health",
            image =  (R.drawable.free_icon_veterinary_2295104),
            route = "health"
        ),
        BarItem(
            title = "Plans",
            image =  (R.drawable.planning),
            route = "plans"
        ),
        BarItem(
            title = "Care",
            image = (R.drawable.specialists),
            route = "care"
        ),
        BarItem(
            title = "Home",
            image = (R.drawable.home),
            route = "home"
        )
    )
}

data class BarItem(
    val title: String,
    val image: Int,
    val route: String
)

@Composable
fun Planning(){
    CalendarView()
}
@Composable
fun Care(){
    Text("About Page", fontSize = 30.sp)
}



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