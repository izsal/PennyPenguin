package com.example.pennypenguin.domain.model

data class Category(
    val id: String,
    val name: String,
    val icon: String,
    val type: TransactionType,
    val isCustom: Boolean = false
) {
    companion object {
        val incomeCategories = listOf(
            Category("inc_salary", "Salary", "payments", TransactionType.INCOME),
            Category("inc_freelance", "Freelance", "work", TransactionType.INCOME),
            Category("inc_bonus", "Bonus", "redeem", TransactionType.INCOME),
            Category("inc_gift", "Gift", "featured_video", TransactionType.INCOME),
            Category("inc_other", "Other", "more_horiz", TransactionType.INCOME)
        )

        val expenseCategories = listOf(
            Category("exp_food", "Food", "restaurant", TransactionType.EXPENSE),
            Category("exp_transport", "Transportation", "directions_car", TransactionType.EXPENSE),
            Category("exp_shopping", "Shopping", "shopping_bag", TransactionType.EXPENSE),
            Category("exp_entertainment", "Entertainment", "movie", TransactionType.EXPENSE),
            Category("exp_bills", "Bills", "receipt_long", TransactionType.EXPENSE),
            Category("exp_health", "Health", "medical_services", TransactionType.EXPENSE),
            Category("exp_education", "Education", "school", TransactionType.EXPENSE),
            Category("exp_other", "Other", "more_horiz", TransactionType.EXPENSE)
        )
        
        val allDefaults = incomeCategories + expenseCategories
    }
}
