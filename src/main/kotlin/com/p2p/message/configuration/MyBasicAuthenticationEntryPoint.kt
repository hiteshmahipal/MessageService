package com.p2p.message.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import org.springframework.stereotype.Component


@Component
class MyBasicAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : BasicAuthenticationEntryPoint() {

    override fun afterPropertiesSet() {
        realmName = "P2PMessageService"
        super.afterPropertiesSet()
    }

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authEx: AuthenticationException
    ) {
        response.addHeader("WWW-Authenticate", "Basic realm=$realmName")
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json;charset=UTF-8"
        val errorDetails: Map<String, String> = mapOf(
            "error" to "Authentication Failed",
            "message" to (authEx.message ?: "Unauthorized")
        )
        response.writer.println(objectMapper.writeValueAsString(errorDetails))
    }


}
