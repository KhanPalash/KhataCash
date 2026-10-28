package com.khanscreations.khatacash.data.repository

import com.khanscreations.khatacash.data.local.BusinessProfileDao
import com.khanscreations.khatacash.data.local.ContactDao
import com.khanscreations.khatacash.data.local.ProductDao
import com.khanscreations.khatacash.data.local.TransactionDao
import com.khanscreations.khatacash.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class KhataRepository(
    private val contactDao: ContactDao,
    private val transactionDao: TransactionDao,
    private val productDao: ProductDao,
    private val businessProfileDao: BusinessProfileDao
) {
    // Contacts
    fun getCustomers(): Flow<List<Contact>> = contactDao.getContactsByType(ContactType.CUSTOMER)
    fun getSuppliers(): Flow<List<Contact>> = contactDao.getContactsByType(ContactType.SUPPLIER)
    fun getContactById(id: Long): Flow<Contact?> = contactDao.getContactByIdFlow(id)

    suspend fun addContact(contact: Contact): Long = contactDao.insertContact(contact)
    suspend fun updateContact(contact: Contact) = contactDao.updateContact(contact)
    suspend fun deleteContact(contact: Contact) = contactDao.deleteContact(contact)

    fun getTotalReceivable(): Flow<Double?> = contactDao.getTotalReceivableFlow()
    fun getTotalPayable(): Flow<Double?> = contactDao.getTotalPayableFlow()

    // Transactions
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()
    fun getTransactionsForContact(contactId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsForContact(contactId)

    fun getExpenses(): Flow<List<Transaction>> =
        transactionDao.getTransactionsByTypes(listOf(TransactionType.EXPENSE))

    fun getTodayStartTimestamp(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun getTodaySales(): Flow<Double?> =
        transactionDao.getSumAmountByTypeSince(TransactionType.SALE_CREDIT, getTodayStartTimestamp())

    fun getTodayCollections(): Flow<Double?> =
        transactionDao.getSumAmountByTypeSince(TransactionType.PAYMENT_RECEIVED, getTodayStartTimestamp())

    fun getTodayExpenses(): Flow<Double?> =
        transactionDao.getSumAmountByTypeSince(TransactionType.EXPENSE, getTodayStartTimestamp())

    suspend fun addTransaction(transaction: Transaction): Long {
        val txId = transactionDao.insertTransaction(transaction)

        // Ledger balance updates for contact
        if (transaction.contactId != null) {
            val contact = contactDao.getContactById(transaction.contactId)
            if (contact != null) {
                val newBalance = when (transaction.type) {
                    TransactionType.SALE_CREDIT -> contact.currentBalance + transaction.amount
                    TransactionType.PAYMENT_RECEIVED -> contact.currentBalance - transaction.amount
                    TransactionType.PURCHASE_CREDIT -> contact.currentBalance + transaction.amount
                    TransactionType.SUPPLIER_PAID -> contact.currentBalance - transaction.amount
                    else -> contact.currentBalance
                }
                contactDao.updateContactBalance(contact.id, newBalance)
            }
        }
        return txId
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        // Reverse balance if contact transaction
        if (transaction.contactId != null) {
            val contact = contactDao.getContactById(transaction.contactId)
            if (contact != null) {
                val reversedBalance = when (transaction.type) {
                    TransactionType.SALE_CREDIT -> contact.currentBalance - transaction.amount
                    TransactionType.PAYMENT_RECEIVED -> contact.currentBalance + transaction.amount
                    TransactionType.PURCHASE_CREDIT -> contact.currentBalance - transaction.amount
                    TransactionType.SUPPLIER_PAID -> contact.currentBalance + transaction.amount
                    else -> contact.currentBalance
                }
                contactDao.updateContactBalance(contact.id, reversedBalance)
            }
        }
        transactionDao.deleteTransaction(transaction)
    }

    // Products
    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()
    suspend fun addProduct(product: Product): Long = productDao.insertProduct(product)
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)
    suspend fun adjustStock(productId: Long, delta: Double) = productDao.adjustStock(productId, delta)

    // Business Profile
    fun getBusinessProfile(): Flow<BusinessProfile?> = businessProfileDao.getProfileFlow()
    suspend fun updateBusinessProfile(profile: BusinessProfile) =
        businessProfileDao.insertOrUpdateProfile(profile)
}
