package com.p2p.message.dto

import com.p2p.message.dao.Username

data class Unread(val username: Username)

data class History(val username: Username, val friend: String)