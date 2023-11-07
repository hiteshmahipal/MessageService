package com.p2p.message.dao

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.p2p.message.dto.Message
import java.util.*

typealias Username = String

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    val username: Username,
    val passcode: String
)

data class UserMessages(
    val sent: MutableList<Message> = Collections.synchronizedList(mutableListOf()),
    val received: MutableList<Message> = Collections.synchronizedList(mutableListOf()),
    var unreadMessagesCount: Int = 0
)
