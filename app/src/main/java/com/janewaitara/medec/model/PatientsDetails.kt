package com.janewaitara.medec.model

data class PatientsDetails(
    var patId: String = "",
    var patientName: String = "",
    var patientEmailAddress: String = "",
    var patientContact: Int = 0,
    var patientImageUrl: String = ""

)