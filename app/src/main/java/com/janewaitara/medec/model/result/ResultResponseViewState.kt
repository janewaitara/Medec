package com.janewaitara.medec.model.result

import com.janewaitara.medec.model.DoctorsDetails

sealed class ResultResponseViewState
class DoctorsListSuccessResult(val data: List<DoctorsDetails>): ResultResponseViewState()
class ResultResponseError(val error: String): ResultResponseViewState()
class UserDocumentExists(val userExists: Boolean): ResultResponseViewState()