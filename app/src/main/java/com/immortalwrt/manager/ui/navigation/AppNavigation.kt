package com.immortalwrt.manager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.immortalwrt.manager.ui.screens.router.RouterListScreen
import com.immortalwrt.manager.ui.screens.router.AddRouterScreen
import com.immortalwrt.manager.ui.screens.dashboard.DashboardScreen
import com.immortalwrt.manager.ui.screens.network.NetworkScreen
import com.immortalwrt.manager.ui.screens.devices.DevicesScreen
import com.immortalwrt.manager.ui.screens.system.SystemScreen
import com.immortalwrt.manager.ui.screens.diagnostics.DiagnosticsScreen
import com.immortalwrt.manager.ui.screens.reboot.RebootScreen
import com.immortalwrt.manager.ui.screens.password.PasswordChangeScreen
import com.immortalwrt.manager.ui.screens.traffic.TrafficScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.RouterList.route) {
        composable(Screen.RouterList.route) {
            RouterListScreen(
                onAddRouter = { navController.navigate(Screen.AddRouter.route) },
                onEditRouter = { routerId -> navController.navigate(Screen.EditRouter.createRoute(routerId)) },
                onRouterClick = { routerId -> navController.navigate(Screen.Dashboard.createRoute(routerId)) }
            )
        }
        composable(Screen.AddRouter.route) {
            AddRouterScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.EditRouter.route,
            arguments = listOf(navArgument("routerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routerId = backStackEntry.arguments?.getString("routerId") ?: return@composable
            AddRouterScreen(routerId = routerId, onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.Dashboard.route,
            arguments = listOf(navArgument("routerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routerId = backStackEntry.arguments?.getString("routerId") ?: return@composable
            DashboardScreen(routerId = routerId)
        }
        composable(
            route = Screen.Network.route,
            arguments = listOf(navArgument("routerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routerId = backStackEntry.arguments?.getString("routerId") ?: return@composable
            NetworkScreen(routerId = routerId)
        }
        composable(
            route = Screen.Devices.route,
            arguments = listOf(navArgument("routerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routerId = backStackEntry.arguments?.getString("routerId") ?: return@composable
            DevicesScreen(routerId = routerId)
        }
        composable(
            route = Screen.System.route,
            arguments = listOf(navArgument("routerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routerId = backStackEntry.arguments?.getString("routerId") ?: return@composable
            SystemScreen(routerId = routerId)
        }
        composable(
            route = Screen.Diagnostics.route,
            arguments = listOf(navArgument("routerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routerId = backStackEntry.arguments?.getString("routerId") ?: return@composable
            DiagnosticsScreen(routerId = routerId)
        }
        composable(
            route = Screen.Reboot.route,
            arguments = listOf(navArgument("routerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routerId = backStackEntry.arguments?.getString("routerId") ?: return@composable
            RebootScreen(routerId = routerId)
        }
        composable(
            route = Screen.PasswordChange.route,
            arguments = listOf(navArgument("routerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routerId = backStackEntry.arguments?.getString("routerId") ?: return@composable
            PasswordChangeScreen(routerId = routerId)
        }
        composable(
            route = Screen.Traffic.route,
            arguments = listOf(navArgument("routerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routerId = backStackEntry.arguments?.getString("routerId") ?: return@composable
            TrafficScreen(routerId = routerId)
        }
    }
}
