package com.janewaitara.medec.model

import java.util.*

interface Message {
    val time: Date
    val messageSenderId: String
    val messageType: String
}

object MessageType{
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}

data class TextMessage(
    val text: String = "",
    override val time: Date = Date(0),
    override val messageSenderId: String = "",
    override val messageType: String = MessageType.TEXT
): Message{
 /*  constructor(): this("", Date(0)," ")*/
}