package com.p2p.message.controller

import com.p2p.message.dao.Senders
import com.p2p.message.dao.User
import com.p2p.message.dto.*
import com.p2p.message.exception.CustomHttpException
import com.p2p.message.service.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/")
class MessageController {

    @Autowired
    private lateinit var messageService: MessageService

    @RequestMapping("send/text/user", method = [RequestMethod.POST])
    fun registerUser(
        @AuthenticationPrincipal user: UserDetails,
        @RequestBody message: Message
    ): ResponseEntity<Any> {
//      Authorisation check
        if (user.username != message.from.id || !user.authorities.contains(SimpleGrantedAuthority(Senders.USER.role))) {
            throw CustomHttpException("Unauthorised user", HttpStatus.FORBIDDEN)
        }
        messageService.send(message)
        return ResponseEntity(
            ResponseDto<User>(
                status = success
            ),
            HttpStatus.CREATED
        )
    }

    @RequestMapping("get/unread", method = [RequestMethod.GET])
    fun getUnread(
        @AuthenticationPrincipal user: UserDetails,
        @RequestBody unread: Unread
    ): ResponseEntity<Any> {

        if (user.username != unread.username) {
            throw CustomHttpException("Unauthorised user", HttpStatus.FORBIDDEN)
        }
        val data = messageService.getUnread(username = user.username)
        return ResponseEntity(
            ResponseDto(
                status = success,
                message = if (data.isEmpty()) {
                    "No new messages"
                } else {
                    "You have message(s)"
                },
                data = data
            ),
            HttpStatus.OK
        )
    }

    @RequestMapping("get/history", method = [RequestMethod.GET])
    fun getHistory(
        @AuthenticationPrincipal user: UserDetails,
        @RequestBody history: History
    ): ResponseEntity<Any> {
        if (user.username != history.username) {
            throw CustomHttpException("Unauthorised user", HttpStatus.FORBIDDEN)
        }


        messageService.getHistory(
            username = user.username,
            friend = Sender(type = Senders.USER, id = history.friend)
        )

        return ResponseEntity(
            ResponseDto(
                status = success,
                data = messageService.getHistory(
                    username = user.username,
                    friend = Sender(type = Senders.USER, id = history.friend)
                )
            ),
            HttpStatus.OK
        )
    }
}
