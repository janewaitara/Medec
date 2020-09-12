package com.janewaitara.medec.model

data class DoctorsDetails(
    var docId: String = "",
    var docName: String = "",
    var doctorsTitle: String = "",
    var docCoursePursued: String = "",
    var doctorEmailAddress: String = "",
    var doctorContact: Int = 0,
    var yearsOfExperience: String = "",
    var docImageUrl: String = "",
    var docLocation: String = ""
)