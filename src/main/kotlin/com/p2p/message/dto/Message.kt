package com.p2p.message.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.p2p.message.dao.Receivers
import com.p2p.message.dao.Senders
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
data class Sender(
    val type: Senders,
    val id: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Receiver(
    val type: Receivers,
    val id: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(
    val text: String,
    val from: Sender,
    val to: Receiver,
    val createdAt: Instant = Instant.now(),
    val expiry: Instant = Instant.MAX
)
