package com.janewaitara.medec.model

import java.util.*

interface Message {
    val time: Date
    val messageSenderId: String
    val messageReceiverId: String
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
    override val messageReceiverId: String = "",
    override val messageType: String = MessageType.TEXT

): Message{
 /*  constructor(): this("", Date(0)," ")*/
}

data class ChannelId(val channelId: String = "")

data class ChatUserDetails(
    var recipientId: String = "",
    var recipientName: String = "",
    var recipientImageUrl: String = "",
    var lastMessage: String = "",
    var recipientLocation: String = ""
)