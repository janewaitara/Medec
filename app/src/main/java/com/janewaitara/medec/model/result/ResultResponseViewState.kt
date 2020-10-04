package com.janewaitara.medec.model.result

import com.janewaitara.medec.model.DoctorsDetails
import com.janewaitara.medec.model.PatientsDetails
import com.janewaitara.medec.model.ChannelId
import com.janewaitara.medec.model.TextMessage

sealed class ResultResponseViewState
class DoctorsListSuccessResult(val data: List<DoctorsDetails>): ResultResponseViewState()
class ResultResponseError(val error: String): ResultResponseViewState()
class UserDocumentExists(val userExists: Boolean): ResultResponseViewState()
class PatientDetailsSuccessResult(val data: PatientsDetails): ResultResponseViewState()
class DoctorDetailsSuccessResult(val data: DoctorsDetails): ResultResponseViewState()
class UserProfileImageUrlReturned(val imageUrl: String): ResultResponseViewState()
class TextMessagesListSuccessResult(val data: List<TextMessage>): ResultResponseViewState()
class LastTextMessageSuccessResult(val textMessage: TextMessage): ResultResponseViewState()
class ChannelIdSuccessResult(val data: String): ResultResponseViewState()
class RecipientsIdSuccessResult(val channelIdList: List<ChannelId>): ResultResponseViewState()
class EmptySuccessMessage(val emptyMessage: String): ResultResponseViewState()
class UserProfilePicUpdateSuccessResult(val message: String): ResultResponseViewState()