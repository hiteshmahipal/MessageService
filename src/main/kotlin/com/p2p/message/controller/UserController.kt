package com.p2p.message.controller

import com.p2p.message.dao.User
import com.p2p.message.dto.ResponseDto
import com.p2p.message.dto.success
import com.p2p.message.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/")
class UserController {

    @Autowired
    private lateinit var userService: IUserService

    @RequestMapping("create/user", method = [RequestMethod.POST])
    fun registerUser(
        @RequestBody userDto: User
    ): ResponseEntity<Any> {
        userService.registerNewUserAccount(userDto = userDto)
        return ResponseEntity(
            ResponseDto<User>(
                status = success
            ),
            HttpStatus.CREATED
        )
    }

    @RequestMapping("get/users", method = [RequestMethod.GET])
    fun getUsers(
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<Any> {
        return ResponseEntity(
            ResponseDto(
                status = success,
                data = userService.getUsers(username = user.username)
            ),
            HttpStatus.OK
        )
    }
}