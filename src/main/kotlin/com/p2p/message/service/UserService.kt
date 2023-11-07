package com.p2p.message.service

import com.p2p.message.dao.User
import com.p2p.message.dao.Username
import com.p2p.message.exception.CustomHttpException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap


@Service
class UserService : IUserService {

    @Autowired
    private lateinit var messageService: MessageService

    companion object {
        private val users: ConcurrentHashMap<Username, User> = ConcurrentHashMap()
    }


    override fun getUser(username: String): User? {
        return users.getOrDefault(username, null)
    }

    override fun getUsers(username: String): List<Username> {
        return users.keys().toList().filter { !it!!.contentEquals(username) }
    }

    override fun registerNewUserAccount(userDto: User) {
        if (users.containsKey(userDto.username)) {
            throw CustomHttpException(message = "User already exists", httpStatusCode = HttpStatus.BAD_REQUEST)
        } else {
            users[userDto.username] = userDto
            messageService.addValidUser(userDto.username)
        }
    }
}
