package com.khanscreations.khatacash.data.local

import androidx.room.*
import com.khanscreations.khatacash.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Long): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("UPDATE products SET currentStock = currentStock + :delta, updatedAt = :timestamp WHERE id = :id")
    suspend fun adjustStock(id: Long, delta: Double, timestamp: Long = System.currentTimeMillis())
}
