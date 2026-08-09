package com.example.pennypenguin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pennypenguin.domain.model.Wallet

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val balance: Double,
    val icon: String
)

fun WalletEntity.toDomain() = Wallet(id, name, balance, icon)
fun Wallet.toEntity() = WalletEntity(id, name, balance, icon)
