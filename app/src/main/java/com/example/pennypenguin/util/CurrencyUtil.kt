package com.example.pennypenguin.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object CurrencyUtil {
    fun formatRupiah(amount: Double): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        val formatted = numberFormat.format(amount)
        
        // Remove .00 if it exists at the end
        return formatted.replace(",00", "").replace("Rp", "Rp ")
    }

    fun formatThousands(amount: String): String {
        if (amount.isEmpty()) return ""
        val cleanString = amount.replace(".", "")
        val parsed = cleanString.toDoubleOrNull() ?: return amount
        
        val formatter = DecimalFormat("#,###")
        val symbols = formatter.decimalFormatSymbols
        symbols.groupingSeparator = '.'
        formatter.decimalFormatSymbols = symbols
        
        return formatter.format(parsed)
    }
}
