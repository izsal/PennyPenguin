package com.example.pennypenguin.util

object Localization {
    private val indonesian = mapOf(
        "dashboard" to "Beranda",
        "transactions" to "Transaksi",
        "reports" to "Laporan",
        "profile" to "Profil",
        "dark_mode" to "Mode Gelap",
        "language" to "Bahasa",
        "privacy_policy" to "Kebijakan Privasi",
        "total_balance" to "Total Saldo",
        "income" to "Pemasukan",
        "expense" to "Pengeluaran",
        "recent_transactions" to "Transaksi Terbaru",
        "see_all" to "Lihat Semua",
        "appearance" to "Tampilan",
        "legal" to "Hukum",
        "waddle_back" to "Selamat datang kembali, %s!",
        "track_fish" to "Ayo Buat Catatan Penny Penguin kamuu!",
        "no_transactions" to "Belum ada transaksi 🐧",
        "add_transaction" to "Tambah Transaksi",
        "save" to "Simpan",
        "note" to "Catatan",
        "amount" to "Jumlah",
        "category" to "Kategori"
    )

    private val english = mapOf(
        "dashboard" to "Dashboard",
        "transactions" to "Transactions",
        "reports" to "Reports",
        "profile" to "Profile",
        "dark_mode" to "Dark Mode",
        "language" to "Language",
        "privacy_policy" to "Privacy Policy",
        "total_balance" to "Total Balance",
        "income" to "Income",
        "expense" to "Expense",
        "recent_transactions" to "Recent Transactions",
        "see_all" to "See All",
        "appearance" to "Appearance",
        "legal" to "Legal",
        "waddle_back" to "Waddle back, %s!",
        "track_fish" to "Let's track your penny penguin today!",
        "no_transactions" to "No transactions yet 🐧",
        "add_transaction" to "Add Transaction",
        "save" to "Save",
        "note" to "Note",
        "amount" to "Amount",
        "category" to "Category"
    )

    fun getString(key: String, lang: String): String {
        return if (lang == "in") indonesian[key] ?: key else english[key] ?: key
    }
}
