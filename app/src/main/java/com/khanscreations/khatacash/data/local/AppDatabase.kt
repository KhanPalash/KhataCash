package com.khanscreations.khatacash.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.khanscreations.khatacash.data.model.BusinessProfile
import com.khanscreations.khatacash.data.model.Contact
import com.khanscreations.khatacash.data.model.ContactType
import com.khanscreations.khatacash.data.model.Product
import com.khanscreations.khatacash.data.model.Transaction
import com.khanscreations.khatacash.data.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Contact::class,
        Transaction::class,
        Product::class,
        BusinessProfile::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun transactionDao(): TransactionDao
    abstract fun productDao(): ProductDao
    abstract fun businessProfileDao(): BusinessProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "khatacash_db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            val profileDao = database.businessProfileDao()
            profileDao.insertOrUpdateProfile(
                BusinessProfile(
                    id = 1,
                    businessName = "খান ব্রাদার্স জেনারেল স্টোর",
                    ownerName = "খান সাহেব",
                    phone = "01711223344",
                    address = "চকবাজার, ঢাকা",
                    businessType = "মুদি ও পাইকারি দোকান",
                    currency = "৳"
                )
            )

            val contactDao = database.contactDao()
            val txDao = database.transactionDao()

            // Sample Customer 1
            val c1Id = contactDao.insertContact(
                Contact(
                    name = "রহিম চৌধুরী",
                    phone = "01812345678",
                    address = "রোড #৪, মিরপুর",
                    type = ContactType.CUSTOMER,
                    currentBalance = 3200.0
                )
            )
            txDao.insertTransaction(
                Transaction(
                    contactId = c1Id,
                    contactName = "রহিম চৌধুরী",
                    contactType = ContactType.CUSTOMER,
                    type = TransactionType.SALE_CREDIT,
                    amount = 5000.0,
                    note = "সয়াবিন তেল ও বাসমতি চাল বাকি"
                )
            )
            txDao.insertTransaction(
                Transaction(
                    contactId = c1Id,
                    contactName = "রহিম চৌধুরী",
                    contactType = ContactType.CUSTOMER,
                    type = TransactionType.PAYMENT_RECEIVED,
                    amount = 1800.0,
                    note = "বিকাশে আংশিক পরিশোধ"
                )
            )

            // Sample Customer 2
            val c2Id = contactDao.insertContact(
                Contact(
                    name = "করিম মিয়া",
                    phone = "01998765432",
                    address = "উত্তরা, ঢাকা",
                    type = ContactType.CUSTOMER,
                    currentBalance = 1450.0
                )
            )
            txDao.insertTransaction(
                Transaction(
                    contactId = c2Id,
                    contactName = "করিম মিয়া",
                    contactType = ContactType.CUSTOMER,
                    type = TransactionType.SALE_CREDIT,
                    amount = 1450.0,
                    note = "মসলা ও চিনি বাকি"
                )
            )

            // Sample Supplier 1
            val s1Id = contactDao.insertContact(
                Contact(
                    name = "প্রাণ-আরএফএল ডিস্ট্রিবিউশন",
                    phone = "01755667788",
                    address = "তেজগাঁও শিল্পাঞ্চল",
                    type = ContactType.SUPPLIER,
                    currentBalance = 8500.0
                )
            )
            txDao.insertTransaction(
                Transaction(
                    contactId = s1Id,
                    contactName = "প্রাণ-আরএফএল ডিস্ট্রিবিউশন",
                    contactType = ContactType.SUPPLIER,
                    type = TransactionType.PURCHASE_CREDIT,
                    amount = 15000.0,
                    note = "নতুন জুস ও স্ন্যাকস ডেলিভারি"
                )
            )
            txDao.insertTransaction(
                Transaction(
                    contactId = s1Id,
                    contactName = "প্রাণ-আরএফএল ডিস্ট্রিবিউশন",
                    contactType = ContactType.SUPPLIER,
                    type = TransactionType.SUPPLIER_PAID,
                    amount = 6500.0,
                    note = "নগদ চেক প্রদান"
                )
            )

            // Sample Expenses
            txDao.insertTransaction(
                Transaction(
                    type = TransactionType.EXPENSE,
                    amount = 1200.0,
                    category = "বিদ্যুৎ বিল",
                    note = "চলতি মাসের বিদ্যুৎ বিল পরিশোধ"
                )
            )
            txDao.insertTransaction(
                Transaction(
                    type = TransactionType.EXPENSE,
                    amount = 500.0,
                    category = "পরিবহন খরচ",
                    note = "পণ্য আনার ভ্যান ভাড়া"
                )
            )

            // Sample Products
            val productDao = database.productDao()
            productDao.insertProduct(
                Product(
                    name = "রূপচাঁদা সয়াবিন তেল ৫ লিটার",
                    unit = "বোতল",
                    purchasePrice = 880.0,
                    sellingPrice = 930.0,
                    currentStock = 18.0,
                    lowStockThreshold = 5.0
                )
            )
            productDao.insertProduct(
                Product(
                    name = "চাষী চিনিগুঁড়া চাল ১ কেজি",
                    unit = "প্যাকেট",
                    purchasePrice = 135.0,
                    sellingPrice = 155.0,
                    currentStock = 45.0,
                    lowStockThreshold = 10.0
                )
            )
            productDao.insertProduct(
                Product(
                    name = "ফ্রেশ আটা ২ কেজি",
                    unit = "প্যাকেট",
                    purchasePrice = 110.0,
                    sellingPrice = 125.0,
                    currentStock = 3.0, // Low stock trigger
                    lowStockThreshold = 8.0
                )
            )
        }
    }
}
