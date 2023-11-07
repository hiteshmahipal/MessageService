package com.p2p.message.service

import com.p2p.message.dao.Receivers
import com.p2p.message.dao.UserMessages
import com.p2p.message.dao.Username
import com.p2p.message.dto.HistoryResponse
import com.p2p.message.dto.Message
import com.p2p.message.dto.Sender
import com.p2p.message.exception.NotFoundException
import com.p2p.message.util.Logging
import com.p2p.message.util.logger
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap


@Service
class MessageService : MServer {

//    Assumptions.
//    1. State is managed for the messages. It's not a simple peer to peer messaging system where message can be sent to only online users.
//    2. Senders and Receivers enums to extend the code for other type of entities like group which will implement only IReceive interface.
//    3. messages at line number 25 is the data structure used to hold sent and received messages.
//    4. Reference to same message(like a foreign key) is added to sender's sent list and receiver's receive list to avoid data duplication.
//    5. CustomHttpException is abused right now in service layer. Can be improved with specific exceptions.

    companion object : Logging {
        private val messages = ConcurrentHashMap<Username, UserMessages>()
        private val log = logger()
    }

    override fun send(m: Message) {
        val userMessages = messages[m.from.id] ?: throw NotFoundException("Sender messages not found")
        userMessages.sent.add(m)
//      This should be atomic or asynchronous operation when there are multiple receivers
        when (m.to.type) {
            Receivers.USER -> receive(m)
        }
    }

    override fun receive(m: Message) {
        val userMessages = messages[m.to.id] ?: throw NotFoundException("Receiver messages not found")
        userMessages.received.add(0, m)
        userMessages.unreadMessagesCount = messages[m.to.id]?.unreadMessagesCount!! + 1
    }

    fun getUnread(username: Username): Map<String, List<String>> {
        val userMessages = messages[username] ?: throw NotFoundException("Receiver messages not found")
        if (userMessages.unreadMessagesCount > 0 && userMessages.received.size >= userMessages.unreadMessagesCount) {
            userMessages.received.iterator()
            val unread = userMessages.received.slice(0 until userMessages.unreadMessagesCount)
            userMessages.unreadMessagesCount = 0
            return unread.groupBy({ it.from.id }, { it.text })
        }
        return emptyMap()
    }

    fun getHistory(username: Username, friend: Sender): HistoryResponse {

        val userMessages = messages[username] ?: throw NotFoundException("Receiver messages not found")

        val sentMessages = userMessages.sent.toList()
        val receivedMessages = userMessages.received.filter { it.from.id == friend.id && it.from.type == friend.type }

        return HistoryResponse(
            texts = sentMessages.plus(receivedMessages).sortedBy { it.createdAt }.map { mapOf(Pair(it.from.id, it.text)) }
        )
    }

    fun addValidUser(username: Username) {
        messages[username] = UserMessages()
    }
}
