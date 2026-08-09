package com.example.pennypenguin.data.repository

import com.example.pennypenguin.data.local.WalletDao
import com.example.pennypenguin.data.local.toDomain
import com.example.pennypenguin.data.local.toEntity
import com.example.pennypenguin.domain.model.Wallet
import com.example.pennypenguin.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val dao: WalletDao
) : WalletRepository {

    override fun getAllWallets(): Flow<List<Wallet>> {
        return dao.getAllWallets().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertWallet(wallet: Wallet) {
        dao.insertWallet(wallet.toEntity())
    }

    override suspend fun updateWallet(wallet: Wallet) {
        dao.updateWallet(wallet.toEntity())
    }

    override suspend fun deleteWallet(wallet: Wallet) {
        dao.deleteWallet(wallet.toEntity())
    }

    override suspend fun getWalletById(id: Int): Wallet? {
        return dao.getWalletById(id)?.toDomain()
    }

    override suspend fun updateBalance(walletId: Int, amount: Double) {
        dao.updateBalance(walletId, amount)
    }

    override fun getTotalBalance(): Flow<Double> {
        return dao.getTotalBalance().map { it ?: 0.0 }
    }
}
