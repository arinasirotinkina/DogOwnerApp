import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dogownerapp.R
import com.example.dogownerapp.presentation.main.health.HealthViewModel
import com.example.dogownerapp.presentation.screen.Health
import com.example.dogownerapp.presentation.screen.customColors
import com.example.dogownerapp.presentation.ui.CustomTheme


@Composable
fun Main() {
    CustomTheme {
        val navController = rememberNavController()
        Column(Modifier.padding(8.dp)) {
            NavHost(navController, startDestination = NavRoutes.Home.route, modifier = Modifier.weight(1f)) {
                composable(NavRoutes.Health.route) { Health(viewModel = HealthViewModel()) }
                composable(NavRoutes.Planning.route) { Planning()  }
                composable(NavRoutes.Care.route) { Care()  }
                composable(NavRoutes.Home.route) { Home() }
            }
            BottomNavigationBar(navController = navController)
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
                        indicatorColor = MaterialTheme.colorScheme.onSurface // Цвет фона активного элемента
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
    Text("Contact Page", fontSize = 30.sp)
}
@Composable
fun Care(){
    Text("About Page", fontSize = 30.sp)
}

@Composable
fun Home(){
    Text("About Page", fontSize = 30.sp)
}

sealed class NavRoutes(val route: String) {
    object Health : NavRoutes("health")
    object Planning : NavRoutes("plans")
    object Care : NavRoutes("care")
    object Home : NavRoutes("home")
}