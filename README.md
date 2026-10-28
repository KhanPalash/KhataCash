# KhataCash (খাতাক্যাশ) — Native Android Mobile App

> **ডিজিটাল খাতা ও ব্যবসার সঠিক হিসাব**  
> Bengali-first Digital Bookkeeping & MSME Financial Management App for Android.

Built according to [KhataCash PRD](file:///media/khanscreations/CINEDATA/KhansCreations/KhataCash/KhataCash_PRD.md) and designed to match the [Figma UI Specification](https://www.figma.com/design/AroxovR752y8YZb7PFs2Ta/KhataCash-%E2%80%94-Mobile-App-UI?node-id=0-1).

---

## 📱 Implemented Figma Screens & Features

### 1. 01 · Home Dashboard (ড্যাশবোর্ড)
- **Top Header**: App branding (`KhataCash`), live online status chip indicator (`●`).
- **Greeting**: "আসসালামু আলাইকুম" with context subtitle "আজকের ব্যবসার হিসাব এক নজরে".
- **Business Switcher Card**: Shop profile card with Bengali avatar (`দ`), shop name ("আমার দোকান"), category ("মুদি দোকান · সিলেট"), and dropdown chevron.
- **Sales & Balance Hero Card**: Emerald `#097A57` card with:
  - "আজকের বিক্রি": `৳ ১২,৫০০` (32sp Bold)
  - "আজ ১৭টি লেনদেন"
  - Two summary metrics: "পাওনা": `৳ ৮,৪০০`, "দেনা": `৳ ৩,২০০` (Amber `#FFE8C9`).
- **Quick Action Grid (দ্রুত হিসাব)**:
  - `↗ বিক্রি` (বাকি বিক্রি লেনদেন)
  - `↓ টাকা পেলাম` (গ্রাহকের জমা আদায়)
  - `＋ কেনা` (সাপ্লায়ার বাকি ক্রয়)
  - `− খরচ` (দোকানের খরচ অন্তর্ভুক্তি)
- **Recent Transactions (আজকের লেনদেন)**:
  - Timeline of recent transactions with contact avatars, item notes, and color-coded amounts (Green for receivables/collection, Red for payables/expenses).

### 2. 02 · Customer & Supplier Ledger (খাতা)
- **Header & Search**: Bengali title with real-time search by customer/supplier name and phone number.
- **Filter Tabs**: `সব`, `কাস্টমার`, and `সাপ্লায়ার` filter pills.
- **Total Balance Card**:
  - মোট পাওনা: `৳ ৪২,৮৫০`
  - মোট দেনা: `৳ ১৮,২০০`
- **Customer List (সাম্প্রতিক হিসাব)**:
  - Contact cards with avatars (`র`, `ক`, `স`, `ম`, `ফ`), last transaction timestamp, current balance, and "পাওনা" / "দেনা" status tags.
  - `＋ নতুন যোগ` button for adding new customers or suppliers with initial balances.

### 3. 03 · Add Transaction (লেনদেন যোগ করুন)
- **Segmented Type Switcher**: `বিক্রি / পাওনা` vs `কেনা / দেনা`.
- **Contact Selector**: Dropdown to select from existing customers/suppliers.
- **Taka Amount Input**: Prominent currency display with `৳` and large digit input.
- **Details & Note**: Optional item/transaction description.
- **Date & Payment Method**: Current Bengali date selector and payment mode (`বাকি` / `নগদ`).
- **Reminder Switch**: "পরিশোধের রিমাইন্ডার পাঠাব" toggle switch.
- **Save Button**: Large green button ("হিসাব সংরক্ষণ করুন") with receipt sharing confirmation.

### 4. 04 · Stock & Inventory (পণ্য ও স্টক)
- Product catalogue with item name, unit (পিস, কেজি, প্যাকেট), purchase price, and selling price.
- Real-time stock counting with low stock alerts (`স্টক কম`).
- Quick `+` and `−` buttons for immediate on-counter stock-in / stock-out.

### 5. 05 · Reports & Statement Sharing (রিপোর্ট)
- Filterable periods: `আজ`, `এই সপ্তাহ`, `এই মাস`.
- Metric cards: Total sales, collections, receivables, payables, and shop expenses.
- **Share Statement Button**: One-tap shareable formatted Bengali account statements for WhatsApp, SMS, and messenger apps.

---

## 🛠️ Architecture & Tech Stack

- **Language**: Kotlin 2.0.21
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + Clean Architecture + Repository Pattern
- **Local Database**: Room 2.6.1 (SQLite offline-first persistence)
- **Navigation**: Jetpack Navigation Compose
- **Concurrency**: Kotlin Coroutines & StateFlow
- **Minimum SDK**: Android 8.0 (API 26)
- **Target / Compile SDK**: Android 15 (API 35)

---

## 🚀 Building & Running

### Build Debug APK:
```bash
./gradlew assembleDebug
```
The compiled APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### Install to Connected Android Device:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
