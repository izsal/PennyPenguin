package com.example.pennypenguin.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtil {
    fun formatRupiah(amount: Double): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(amount).replace("Rp", "Rp ")
    }
}
