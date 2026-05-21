package com.immortalwrt.manager.ui.navigation

sealed class Screen(val route: String) {
    data object RouterList : Screen("router_list")
    data object AddRouter : Screen("add_router")
    data object EditRouter : Screen("edit_router/{routerId}") {
        fun createRoute(routerId: String) = "edit_router/$routerId"
    }
    data object Dashboard : Screen("dashboard/{routerId}") {
        fun createRoute(routerId: String) = "dashboard/$routerId"
    }
    data object Network : Screen("network/{routerId}") {
        fun createRoute(routerId: String) = "network/$routerId"
    }
    data object Devices : Screen("devices/{routerId}") {
        fun createRoute(routerId: String) = "devices/$routerId"
    }
    data object System : Screen("system/{routerId}") {
        fun createRoute(routerId: String) = "system/$routerId"
    }
    data object Diagnostics : Screen("diagnostics/{routerId}") {
        fun createRoute(routerId: String) = "diagnostics/$routerId"
    }
    data object Traffic : Screen("traffic/{routerId}") {
        fun createRoute(routerId: String) = "traffic/$routerId"
    }
    data object Reboot : Screen("reboot/{routerId}") {
        fun createRoute(routerId: String) = "reboot/$routerId"
    }
    data object PasswordChange : Screen("password_change/{routerId}") {
        fun createRoute(routerId: String) = "password_change/$routerId"
    }
}
