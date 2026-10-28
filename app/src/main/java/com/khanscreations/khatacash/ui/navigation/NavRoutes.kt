package com.khanscreations.khatacash.ui.navigation

sealed class Screen(val route: String, val title: String) {
    data object Dashboard : Screen("dashboard", "ড্যাশবোর্ড")
    data object Customers : Screen("customers", "কাস্টমার")
    data object CustomerDetail : Screen("customer_detail/{contactId}", "কাস্টমার খাতা") {
        fun createRoute(contactId: Long) = "customer_detail/$contactId"
    }
    data object Suppliers : Screen("suppliers", "সাপ্লায়ার")
    data object SupplierDetail : Screen("supplier_detail/{contactId}", "সাপ্লায়ার খাতা") {
        fun createRoute(contactId: Long) = "supplier_detail/$contactId"
    }
    data object Cashbox : Screen("cashbox", "ক্যাশ ও খরচ")
    data object Inventory : Screen("inventory", "পণ্য ও স্টক")
    data object Reports : Screen("reports", "রিপোর্ট")
    data object Settings : Screen("settings", "সেটিংস")
}
