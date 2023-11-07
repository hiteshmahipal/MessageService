package com.p2p.message.exception

import org.springframework.http.HttpStatusCode

class CustomHttpException(override val message: String, val httpStatusCode: HttpStatusCode) : RuntimeException()

class NotFoundException(override val message: String) : RuntimeException()