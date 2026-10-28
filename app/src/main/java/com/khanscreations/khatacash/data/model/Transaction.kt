package com.khanscreations.khatacash.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class TransactionType(val banglaLabel: String) {
    SALE_CREDIT("বাকি বিক্রি"),
    PAYMENT_RECEIVED("টাকা পেলাম"),
    PURCHASE_CREDIT("বাকি ক্রয়"),
    SUPPLIER_PAID("টাকা দিলাম"),
    INCOME_CASH("নগদ বিক্রি"),
    EXPENSE("দোকান খরচ")
}

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientTxId: String = UUID.randomUUID().toString(),
    val contactId: Long? = null,
    val contactName: String? = null,
    val contactType: ContactType? = null,
    val type: TransactionType,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val category: String? = null,
    val note: String? = null,
    val billImageUrl: String? = null,
    val isSynced: Boolean = true
)
