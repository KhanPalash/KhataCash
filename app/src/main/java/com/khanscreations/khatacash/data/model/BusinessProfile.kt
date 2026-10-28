package com.khanscreations.khatacash.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business_profile")
data class BusinessProfile(
    @PrimaryKey
    val id: Int = 1,
    val businessName: String = "আমার দোকান",
    val ownerName: String = "প্রোপ্রাইটর",
    val phone: String = "01700000000",
    val address: String = "ঢাকা, বাংলাদেশ",
    val businessType: String = "মুদি দোকান",
    val currency: String = "৳",
    val isBengali: Boolean = true,
    val isPinEnabled: Boolean = false,
    val securityPin: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
