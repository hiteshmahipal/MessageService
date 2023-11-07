package com.p2p.message.configuration

import com.p2p.message.dao.Senders
import com.p2p.message.exception.CustomHttpException
import com.p2p.message.exception.NotFoundException
import com.p2p.message.service.IUserService
import org.apache.logging.log4j.util.Strings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class SpringUserDetails : UserDetailsService {

    @Autowired
    private lateinit var userService: IUserService

    companion object {
        private fun getAuthorities(roles: List<String>): List<GrantedAuthority>? {
            val authorities: MutableList<GrantedAuthority> = ArrayList()
            for (role in roles) {
                authorities.add(SimpleGrantedAuthority(role))
            }
            return authorities
        }
    }


    override fun loadUserByUsername(username: String?): UserDetails {

        if (Strings.isBlank(username)) {
            throw CustomHttpException("blank user name", HttpStatus.BAD_REQUEST)
        }
        val user = userService.getUser(username!!) ?: throw NotFoundException("User not found")
        val enabled = true
        val accountNonExpired = true
        val credentialsNonExpired = true
        val accountNonLocked = true
        return User(
            user.username, BCryptPasswordEncoder().encode(user.passcode), enabled, accountNonExpired,
            credentialsNonExpired, accountNonLocked, getAuthorities(listOf(Senders.USER.role))
        )
    }
}
