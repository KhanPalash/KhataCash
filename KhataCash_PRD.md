# KhataCash — Product Requirements Document (PRD)

**Document status:** Initial product specification  
**Version:** 1.0  
**Date:** October 28, 2026  
**Product:** KhataCash — digital bookkeeping and payment companion for small businesses  
**Initial market:** Bangladesh  
**Initial platform:** Android mobile app; iOS and web dashboard may be considered later

> This PRD uses the business problems and common feature set of products such as TallyKhata as a reference for defining KhataCash. KhataCash will have its own name, UI, copy, visual design, code, and brand assets; it will not be a direct copy of another product. Before launching payments, QR, or wallet features, confirm the required licensed financial partners and regulatory conditions.

## 1. Product Summary

KhataCash will help small shops and micro-businesses in Bangladesh keep track of sales, purchases, receivables, payables, expenses, products, inventory, and customer/supplier accounts. The app will be Bengali-first, easy to use, fast, and suitable for recording everyday transactions even when the user is offline.

The first release will focus on **digital bookkeeping and business accounts**. QR payments and a digital wallet are planned as a later, partner-dependent phase. Until licensing and integrations are confirmed, the MVP will not promise to hold or transfer money.

## 2. Problem Statement

Small business owners often spread their records across paper ledgers, calculators, and messages. As a result:

- It is difficult to quickly see how much each customer owes or how much is owed to each supplier.
- Preparing daily sales, purchase, and expense summaries takes time.
- Old receivables may be forgotten, and customers may disagree about transaction records.
- Transaction history can be lost if a ledger is damaged or misplaced.
- It is difficult to understand product availability and inventory value.
- Complex accounting software can be hard to use for Bengali-speaking business owners who may not know formal accounting concepts.

## 3. Target Users

### Primary users

- Grocery and general store owners
- Pharmacies, clothing shops, variety stores, and small supermarkets
- Cafés, restaurants, and home-based or online sellers
- Sole proprietors or family members who maintain accounts for the same business

### User characteristics

- Comfortable working in Bengali
- Uses an Android phone; device and network quality may vary
- Wants to record transactions quickly
- May not be an accountant; users should not need to understand concepts such as debit and credit

### Later-stage users

- Owners of multiple businesses or branches
- Owners who want to give employees limited access
- Businesses that want to accept Bangla QR or digital collections

## 4. Product Goals and Success Metrics

### Goals

1. Help a new user add their first customer and transaction within three minutes.
2. Keep reliable digital records of sales, purchases, expenses, receivables, and payables.
3. Make current customer/supplier balances and daily business summaries easy to understand.
4. Allow users to enter transactions without losing data when offline.
5. Help collect overdue payments through reminders and shareable account statements.

### Initial KPIs

These are proposed targets and may be adjusted after a baseline is established.

- 70% of new users who complete onboarding add at least one transaction within 24 hours.
- 50% of weekly active users use the app on at least three days in their first week.
- Median transaction-entry time is under 20 seconds.
- Successful sync rate is at least 99%; duplicate-transaction rate is below 0.1%.
- Improve 30-day retention and the share of businesses recording transactions each week over time.
- Backup/restore success rate is at least 99%.

## 5. Product Principles

- **Bengali-first:** Bengali is the default language; a simple English option may also be provided.
- **Fast entry:** Core tasks should take few steps, with clear labels and large touch targets.
- **Transparent accounting:** Users can understand how each balance was calculated by reviewing transaction history.
- **Offline-tolerant:** Users can create entries without a connection and sync them safely when connectivity returns.
- **Privacy and security:** Collect only necessary information and provide secure sign-in and data encryption.
- **Distinct identity:** KhataCash will use its own name, icon, colors, copy, and UI patterns.

## 6. MVP Scope

### Included

- Sign-in and OTP verification using a mobile number
- Create and edit a business profile
- Customer and supplier lists with separate ledgers
- Credit sales/receivables, payment collection, credit purchases/payables, and supplier payments
- Income and expense entries
- Current balances and transaction timelines
- Daily/monthly summaries and basic reports
- Shareable receipts/statements and overdue reminders
- Basic product list and stock-in/stock-out tracking
- Offline entry and background synchronization
- Cloud backup and restore on another device
- App PIN or biometric lock on supported devices

### Excluded from the MVP

- KhataCash holding wallet balances or transferring money itself
- QR payment settlement or card processing
- Automatic bank feeds or reconciliation
- Full POS, barcode scanning, or printer integration
- Live stock sync across multiple branches
- Loans, credit scores, financial advice, or credit approval
- Complex VAT/tax returns or ERP features

## 7. User Flows

### 7.1 First-time use

1. Open the app and choose a language (Bengali by default).
2. Verify a mobile number using OTP.
3. Add business name, type, and optional address.
4. See a “Record your first transaction” prompt on the home screen.
5. Add a customer/supplier or enter a name to start a transaction quickly.
6. After saving the first transaction, see the updated balance and sharing options.

### 7.2 Sell on credit to a customer

1. Select “Sale/Receivable.”
2. Select a customer or add a new one.
3. Enter the amount, date, optional product/details, and notes.
4. Save; the amount due is added to the customer’s balance.
5. Optionally share a receipt/account summary by SMS or another sharing app.

### 7.3 Receive a payment

1. Open a customer ledger and select “Payment received.”
2. Enter the amount, date, payment method (cash/other/custom), and notes.
3. Save; the receivable is reduced while the original transaction history remains intact.
4. Optionally share a receipt.

### 7.4 Purchase from and pay a supplier

Using the same principles, a “Purchase/Payable” transaction increases the amount owed, while a “Payment to supplier” transaction reduces it.

### 7.5 Overdue reminder

1. Select “Send reminder” from a customer account or the receivables list.
2. Review or edit a prefilled Bengali message and outstanding amount.
3. Send it using the user’s preferred sharing app.
4. If direct SMS sending is offered in the MVP, disclose any charges and required permissions; otherwise use the operating system’s share sheet.

## 8. Functional Requirements

### 8.1 Account and business

- Users must verify a mobile number using OTP.
- Users can set a business name, type, address, currency (BDT), and optional profile image.
- Provide logout, verified phone-number change, account-deletion request, and data-export options.

### 8.2 Customers and suppliers

- Name is required; phone number, address, and notes are optional.
- Users can search by name/number and sort by receivables/payables.
- If one person is both a customer and a supplier, the ledgers must remain separate or a clearly defined combined-profile policy must be used. In the MVP, the two ledger types will be clearly separated.
- Users can archive a contact. If transactions exist, warn before permanent deletion and provide an account-export option.

### 8.3 Transactions

- Transaction types: credit sale/receivable, cash sale (optional MVP), payment received, credit purchase/payable, supplier payment, income, expense, and opening-balance adjustment.
- Each entry must include amount, date/time, related person where relevant, description/notes, and creation/sync status.
- Users can edit or void incorrect entries. Record the change time and previous value in an audit log. Prefer void/reversal over deletion.
- Amounts must be greater than zero and within configured limits; show clear form validation.
- Calculate balances from transactions. Avoid a separate manually editable balance. If an opening balance is needed, record it as a distinct “Opening balance” transaction.
- Give each offline entry a stable client-generated ID to prevent duplicate submissions.

### 8.4 Products and inventory (limited MVP version)

- Product name is required; unit, purchase price, selling price, and opening stock are optional.
- Stock-in/out entries must include quantity and reason.
- Warn before stock goes below zero; make the policy configurable in business settings.
- The MVP does not include bulk import, batches/expiry dates, multiple warehouses, or automatic FIFO valuation.

### 8.5 Reports and sharing

- The home screen must show today’s sales, collections, expenses, and total receivables/payables.
- Users can filter transactions by date range.
- Customer/supplier statements must show transactions, total balance, and covered period.
- Generate a PDF or shareable summary; Bengali fonts must render correctly.
- Show a preview before sharing reports that contain personal information.

### 8.6 Offline use and synchronization

- New transactions, contacts, and products can be saved in encrypted local storage while offline.
- Sync automatically when connectivity returns; users can see sync status.
- If the same record is edited on multiple devices, do not silently overwrite data. Detect conflicts and resolve them safely or present them clearly to the user.
- If sync fails, keep data locally, retry, and show the user its status.

### 8.7 Security and backup

- OTP-based sign-in; app lock with PIN/biometrics on supported devices.
- Protect data in transit using TLS and encrypt sensitive data on the server.
- Backups are linked to the account and can be restored on a new device after verified sign-in.
- Before account deletion, explain its effects, including whether backups will also be deleted.
- Apply rate limits to unusual logins, OTP abuse, and API abuse.

## 9. Proposed Scope for Later Releases

### V1.1 — Business management improvements

- Gold/premium subscription structure, after demand validation
- Multiple business profiles
- Unlimited transactions and advanced inventory reports
- Group reminders
- CSV/PDF exports and advanced reports
- Employee access and roles

### V2 — Payment ecosystem (subject to partner and regulatory approval)

- Accept Bangla QR payments through an approved payment partner
- Payment-success notifications and transaction reconciliation
- Payment links and collection statements
- Wallet, transfer, and recharge features through a partner only if contracts, fees, and licensing allow them

## 10. Non-functional Requirements

- **Performance:** Load common home/ledger views within two seconds on typical phones; give immediate feedback when saving a transaction.
- **Reliability:** Initial target of at least 99.5% crash-free sessions.
- **Accessibility:** Support larger text, adequate contrast, screen-reader labels, and large touch targets.
- **Localization:** Full Bengali support; handle Bengali/English number formats and dates consistently and clearly.
- **Low data usage:** Batch background sync and limit unnecessary network/image use.
- **Scale:** Maintain usable performance for large ledgers using pagination and search.
- **Monitoring:** Track errors, sync, OTP, and report-generation metrics; do not log transaction details unless necessary.

## 11. UX and Screen List

1. Language/welcome
2. Phone number and OTP
3. Business profile setup
4. Home/today’s summary
5. Quick transaction entry (sale, collection, purchase, payment, expense)
6. Customer list and customer ledger
7. Supplier list and supplier ledger
8. Transaction details/edit
9. Product list, product details, and stock adjustment
10. Reports and date filters
11. Share/reminder preview
12. Notifications and sync status
13. Settings, security, backup/restore, data export, and account deletion

## 12. Acceptance Criteria

- A new user can create an account after OTP verification and entering a business name.
- Recording a receivable under a customer updates that customer’s ledger with the correct balance and timeline.
- Recording a payment correctly reduces the receivable while preserving the prior transaction history.
- Entries recorded offline remain after closing/reopening the app and sync once when the network returns.
- Resubmitting the same offline entry does not create a duplicate transaction.
- Date-filtered reports and statement totals match the ledger transactions.
- Bengali names, numbers, PDFs, and shared statements remain readable.
- A user can restore their account data on another device after verifying the account.
- Users can review the recipient, amount, and message before sending a reminder.
- Account-deletion and data-export requests are explicitly confirmed and can be tracked.

## 13. Risks and Dependencies

- **Accounting errors:** Unclear transaction types or balance rules can undermine trust. Use simple Bengali labels and ledger-based calculations.
- **Offline conflicts:** Concurrent edits across devices can cause incorrect merges. Client IDs, audit logs, and conflict resolution are required.
- **Data loss:** Backup and restore reliability is central to user trust; test the restore flow on real devices.
- **Payment regulation:** QR, wallets, money transfers, and customer verification may require approved partners or licenses. This PRD does not assume regulatory approval.
- **SMS cost/permissions:** Direct SMS may depend on carrier fees, consent, and delivery reliability.
- **User trust:** Financial-record privacy, data deletion, and clear terms of use are essential.
- **Feature limits:** Validate user demand and pricing before deciding which features are free or paid.

## 14. Decisions to Make Before Implementation

1. Should the app be an offline-friendly ledger only, or should server sync be required from launch?
2. Android is the first release; what is the minimum supported Android version?
3. Will sign-in use phone number/OTP only, or will users also re-enter with a PIN?
4. Will products/inventory be included in the MVP? If so, how many SKUs are free?
5. What is the initial revenue model: free, subscription, or decided later?
6. Will reminders send SMS directly, or use WhatsApp/the share sheet first?
7. Which approved partner, if any, will support payments/QR?
8. In which release are employee accounts, multiple businesses, and multiple devices required?

## 15. References

- [TallyKhata features and ledger](https://www.tallykhata.com/tallykhata-features/)
- [TallyKhata official home](https://www.tallykhata.com/)
- [TallyPay features](https://www.tallykhata.com/tallypay-features/)
- [TallyKhata FAQ](https://www.tallykhata.com/faq/)
- [Google Play — TallyKhata: Accounts & Bangla QR](https://play.google.com/store/apps/details?id=com.progoti.tallykhata)

---

**Note:** This is an initial PRD. Before engineering begins, define the data model and balance rules, OTP and backup infrastructure, payment partners, fees, and inventory limits for the MVP.
