package com.khanscreations.khatacash.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val unit: String = "পিস", // কেজি, পিস, লিটার, প্যাকেট, ডজন
    val purchasePrice: Double = 0.0,
    val sellingPrice: Double = 0.0,
    val currentStock: Double = 0.0,
    val lowStockThreshold: Double = 5.0,
    val updatedAt: Long = System.currentTimeMillis()
)
