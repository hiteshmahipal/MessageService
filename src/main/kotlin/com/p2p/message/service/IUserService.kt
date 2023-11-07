package com.p2p.message.service

import com.p2p.message.dao.User
import com.p2p.message.dao.Username

interface IUserService {
    fun getUser(username: String): User?
    fun getUsers(username: String): List<Username>
    fun registerNewUserAccount(userDto: User)
}