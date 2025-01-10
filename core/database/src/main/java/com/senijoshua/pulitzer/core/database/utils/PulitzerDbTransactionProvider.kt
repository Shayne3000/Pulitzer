package com.senijoshua.pulitzer.core.database.utils

/**
 * Interface through which higher elements in the architectural
 * hierarchy can execute multiple DB operations as a single,
 * atomic unit within a DB transaction.
 */
interface PulitzerDbTransactionProvider {
    suspend fun withTransaction(operationsToExecute: suspend () -> Unit)
}
