package com.example.pennypenguin.domain.repository

import com.example.pennypenguin.domain.model.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getAllWallets(): Flow<List<Wallet>>
    suspend fun insertWallet(wallet: Wallet)
    suspend fun updateWallet(wallet: Wallet)
    suspend fun deleteWallet(wallet: Wallet)
    suspend fun getWalletById(id: Int): Wallet?
    suspend fun updateBalance(walletId: Int, amount: Double)
    fun getTotalBalance(): Flow<Double>
}
