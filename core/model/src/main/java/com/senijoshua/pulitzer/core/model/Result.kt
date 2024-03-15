package com.senijoshua.pulitzer.core.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Class that encapsulates the result of a data retrieval operation and
 * communicates said result to the presentation layer.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: Throwable) : Result<Nothing>()
}

/**
 * Flow Intermediary operator extension function to parameterize the data
 * type, T (from a Flow) inside a Result type and then parameterize that Result type
 * within a Flow.
 *
 * In short, it converts Flow<T> to Flow<Result<T>>.
 */
fun <T> Flow<T>.toResult(): Flow<Result<T>> = this.map<T, Result<T>> {
    Result.Success(it)
}.catch {
    emit(Result.Error(it))
}
