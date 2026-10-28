package com.khanscreations.khatacash

import android.app.Application
import com.khanscreations.khatacash.data.local.AppDatabase
import com.khanscreations.khatacash.data.repository.KhataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class KhataCashApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {
        KhataRepository(
            contactDao = database.contactDao(),
            transactionDao = database.transactionDao(),
            productDao = database.productDao(),
            businessProfileDao = database.businessProfileDao()
        )
    }
}
