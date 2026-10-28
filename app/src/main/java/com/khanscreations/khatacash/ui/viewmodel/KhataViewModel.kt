package com.khanscreations.khatacash.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.khanscreations.khatacash.data.model.*
import com.khanscreations.khatacash.data.repository.KhataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class KhataViewModel(
    private val repository: KhataRepository
) : ViewModel() {

    val customers: StateFlow<List<Contact>> = repository.getCustomers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val suppliers: StateFlow<List<Contact>> = repository.getSuppliers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalReceivable: StateFlow<Double> = repository.getTotalReceivable()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalPayable: StateFlow<Double> = repository.getTotalPayable()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val todaySales: StateFlow<Double> = repository.getTodaySales()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val todayCollections: StateFlow<Double> = repository.getTodayCollections()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val todayExpenses: StateFlow<Double> = repository.getTodayExpenses()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val allTransactions: StateFlow<List<Transaction>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<Transaction>> = repository.getExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<Product>> = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val businessProfile: StateFlow<BusinessProfile?> = repository.getBusinessProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun getTransactionsForContact(contactId: Long): Flow<List<Transaction>> =
        repository.getTransactionsForContact(contactId)

    fun getContactById(contactId: Long): Flow<Contact?> =
        repository.getContactById(contactId)

    // Contact Actions
    fun addCustomer(name: String, phone: String, address: String?, initialBalance: Double = 0.0) {
        viewModelScope.launch {
            val contact = Contact(
                name = name.trim(),
                phone = phone.trim(),
                address = address?.trim(),
                type = ContactType.CUSTOMER,
                currentBalance = initialBalance
            )
            val id = repository.addContact(contact)
            if (initialBalance > 0) {
                repository.addTransaction(
                    Transaction(
                        contactId = id,
                        contactName = name,
                        contactType = ContactType.CUSTOMER,
                        type = TransactionType.SALE_CREDIT,
                        amount = initialBalance,
                        note = "পূর্বের বাকি (শুরুর হিসাব)"
                    )
                )
            }
        }
    }

    fun addSupplier(name: String, phone: String, address: String?, initialBalance: Double = 0.0) {
        viewModelScope.launch {
            val contact = Contact(
                name = name.trim(),
                phone = phone.trim(),
                address = address?.trim(),
                type = ContactType.SUPPLIER,
                currentBalance = initialBalance
            )
            val id = repository.addContact(contact)
            if (initialBalance > 0) {
                repository.addTransaction(
                    Transaction(
                        contactId = id,
                        contactName = name,
                        contactType = ContactType.SUPPLIER,
                        type = TransactionType.PURCHASE_CREDIT,
                        amount = initialBalance,
                        note = "পূর্বের পাওনা (শুরুর হিসাব)"
                    )
                )
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    // Transaction Actions
    fun addSaleCredit(contactId: Long, contactName: String, amount: Double, note: String?) {
        viewModelScope.launch {
            repository.addTransaction(
                Transaction(
                    contactId = contactId,
                    contactName = contactName,
                    contactType = ContactType.CUSTOMER,
                    type = TransactionType.SALE_CREDIT,
                    amount = amount,
                    note = note
                )
            )
        }
    }

    fun addPaymentReceived(contactId: Long, contactName: String, amount: Double, note: String?) {
        viewModelScope.launch {
            repository.addTransaction(
                Transaction(
                    contactId = contactId,
                    contactName = contactName,
                    contactType = ContactType.CUSTOMER,
                    type = TransactionType.PAYMENT_RECEIVED,
                    amount = amount,
                    note = note
                )
            )
        }
    }

    fun addPurchaseCredit(contactId: Long, contactName: String, amount: Double, note: String?) {
        viewModelScope.launch {
            repository.addTransaction(
                Transaction(
                    contactId = contactId,
                    contactName = contactName,
                    contactType = ContactType.SUPPLIER,
                    type = TransactionType.PURCHASE_CREDIT,
                    amount = amount,
                    note = note
                )
            )
        }
    }

    fun addSupplierPaid(contactId: Long, contactName: String, amount: Double, note: String?) {
        viewModelScope.launch {
            repository.addTransaction(
                Transaction(
                    contactId = contactId,
                    contactName = contactName,
                    contactType = ContactType.SUPPLIER,
                    type = TransactionType.SUPPLIER_PAID,
                    amount = amount,
                    note = note
                )
            )
        }
    }

    fun addExpense(amount: Double, category: String, note: String?) {
        viewModelScope.launch {
            repository.addTransaction(
                Transaction(
                    type = TransactionType.EXPENSE,
                    amount = amount,
                    category = category,
                    note = note
                )
            )
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    // Inventory Actions
    fun addProduct(name: String, unit: String, purchasePrice: Double, sellingPrice: Double, stock: Double) {
        viewModelScope.launch {
            repository.addProduct(
                Product(
                    name = name.trim(),
                    unit = unit.trim(),
                    purchasePrice = purchasePrice,
                    sellingPrice = sellingPrice,
                    currentStock = stock
                )
            )
        }
    }

    fun adjustStock(productId: Long, delta: Double) {
        viewModelScope.launch {
            repository.adjustStock(productId, delta)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    // Profile Settings
    fun updateBusinessProfile(
        businessName: String,
        ownerName: String,
        phone: String,
        address: String,
        businessType: String
    ) {
        viewModelScope.launch {
            val current = businessProfile.value ?: BusinessProfile()
            repository.updateBusinessProfile(
                current.copy(
                    businessName = businessName,
                    ownerName = ownerName,
                    phone = phone,
                    address = address,
                    businessType = businessType,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}

class KhataViewModelFactory(private val repository: KhataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KhataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KhataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
