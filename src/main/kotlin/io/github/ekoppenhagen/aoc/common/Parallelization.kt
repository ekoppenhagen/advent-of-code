package io.github.ekoppenhagen.aoc.common

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend inline fun <T, R> Iterable<T>.mapParallel(crossinline transform: (T) -> R) =
    coroutineScope {
        map { value ->
            async { transform(value) }
        }.map { it.await() }
    }

suspend fun <T> Iterable<T>.filterParallel(predicate: suspend (T) -> Boolean) =
    coroutineScope {
        map { value ->
            async { value.takeIf { predicate(it) } }
        }.awaitAll().filterNotNull()
    }