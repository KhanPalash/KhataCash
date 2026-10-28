package com.khanscreations.khatacash.data.local

import androidx.room.*
import com.khanscreations.khatacash.data.model.BusinessProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessProfileDao {
    @Query("SELECT * FROM business_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<BusinessProfile?>

    @Query("SELECT * FROM business_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): BusinessProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: BusinessProfile)
}
