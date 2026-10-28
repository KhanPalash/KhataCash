package com.khanscreations.khatacash.data.model

data class DashboardSummary(
    val totalReceivable: Double = 0.0, // মোট পাওনা
    val totalPayable: Double = 0.0,    // মোট দেনা
    val todaySales: Double = 0.0,       // আজকের বিক্রি
    val todayCollection: Double = 0.0,  // আজকের জমা
    val todayExpense: Double = 0.0,     // আজকের খরচ
    val currentCash: Double = 0.0,      // ক্যাশ ড্রয়ার/হাতে নগদ
    val customerCount: Int = 0,
    val supplierCount: Int = 0
)

data class ReportSummary(
    val startDate: Long,
    val endDate: Long,
    val totalCreditSales: Double = 0.0,
    val totalCashSales: Double = 0.0,
    val totalCollections: Double = 0.0,
    val totalPurchases: Double = 0.0,
    val totalSupplierPayments: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netProfitEstimated: Double = 0.0
)
