package com.khanscreations.khatacash.data.local

import androidx.room.*
import com.khanscreations.khatacash.data.model.Transaction
import com.khanscreations.khatacash.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE contactId = :contactId ORDER BY date DESC")
    fun getTransactionsForContact(contactId: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type IN (:types) ORDER BY date DESC")
    fun getTransactionsByTypes(types: List<TransactionType>): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE date >= :startTime AND date <= :endTime ORDER BY date DESC")
    fun getTransactionsInRange(startTime: Long, endTime: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionById(id: Long): Transaction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date >= :startTime")
    fun getSumAmountByTypeSince(type: TransactionType, startTime: Long): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date >= :startTime AND date <= :endTime")
    suspend fun getSumAmountByTypeBetween(type: TransactionType, startTime: Long, endTime: Long): Double?
}
