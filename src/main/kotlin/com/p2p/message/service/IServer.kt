package com.p2p.message.service

import com.p2p.message.dto.Message

interface ISend {
    fun send(m: Message)
}

interface IReceive {
    fun receive(m: Message)
}

interface MServer : ISend, IReceive
