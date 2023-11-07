package com.p2p.message.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto<V>(
    val status: String,
    val message: String? = null,
    val data: V? = null
)

data class HistoryResponse(
    val texts: List<Map<String, String>>
)
