package com.janewaitara.medec.model.result

import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.PatientsDetails
import com.janewaitara.medec.model.TextMessage

sealed class ResultResponseViewState
class DoctorsListSuccessResult(val data: List<DoctorsDetails>): ResultResponseViewState()
class ResultResponseError(val error: String): ResultResponseViewState()
class UserDocumentExists(val userExists: Boolean): ResultResponseViewState()
class PatientDetailsSuccessResult(val data: PatientsDetails): ResultResponseViewState()
class UserProfileImageUrlReturned(val imageUrl: String): ResultResponseViewState()
class TextMessagesListSuccessResult(val data: List<TextMessage>): ResultResponseViewState()
class ChannelIdSuccessResult(val data: String): ResultResponseViewState()