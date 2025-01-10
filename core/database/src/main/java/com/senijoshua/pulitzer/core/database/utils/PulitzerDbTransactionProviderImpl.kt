package com.senijoshua.pulitzer.core.database.utils

import androidx.room.withTransaction
import com.senijoshua.pulitzer.core.database.PulitzerDatabase
import javax.inject.Inject

internal class PulitzerDbTransactionProviderImpl @Inject constructor(
    private val db: PulitzerDatabase
) : PulitzerDbTransactionProvider {
    override suspend fun withTransaction(operationsToExecute: suspend () -> Unit) {
        db.withTransaction {
            operationsToExecute()
        }
    }
}
