package com.khanscreations.khatacash.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ContactType {
    CUSTOMER, // কাস্টমার
    SUPPLIER  // সাপ্লায়ার
}

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val address: String? = null,
    val type: ContactType = ContactType.CUSTOMER,
    val currentBalance: Double = 0.0,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
