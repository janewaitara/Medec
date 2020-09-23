package com.janewaitara.medec.model.result

sealed class Result <out T>

data class Success <out T: Any>(val data: T): Result<T>()

data class Failure (val error: Throwable): Result<Nothing>()

data class EmptySuccess(val message: String): Result<Nothing>()

class Loading<T> : Result<T>()