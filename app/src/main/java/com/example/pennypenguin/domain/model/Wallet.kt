package com.example.pennypenguin.domain.model

data class Wallet(
    val id: Int = 0,
    val name: String,
    val balance: Double,
    val icon: String,
    val backgroundImageUri: String? = null
)
