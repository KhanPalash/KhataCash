package com.khanscreations.khatacash.data.local

import androidx.room.*
import com.khanscreations.khatacash.data.model.Contact
import com.khanscreations.khatacash.data.model.ContactType
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts WHERE type = :type ORDER BY updatedAt DESC")
    fun getContactsByType(type: ContactType): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    suspend fun getContactById(id: Long): Contact?

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    fun getContactByIdFlow(id: Long): Flow<Contact?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact): Long

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("UPDATE contacts SET currentBalance = :newBalance, updatedAt = :timestamp WHERE id = :id")
    suspend fun updateContactBalance(id: Long, newBalance: Double, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT SUM(CASE WHEN currentBalance > 0 THEN currentBalance ELSE 0 END) FROM contacts WHERE type = 'CUSTOMER'")
    fun getTotalReceivableFlow(): Flow<Double?>

    @Query("SELECT SUM(CASE WHEN currentBalance > 0 THEN currentBalance ELSE 0 END) FROM contacts WHERE type = 'SUPPLIER'")
    fun getTotalPayableFlow(): Flow<Double?>
}
