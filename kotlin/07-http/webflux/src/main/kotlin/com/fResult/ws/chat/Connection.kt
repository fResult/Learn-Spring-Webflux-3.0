package com.fResult.ws.chat

import org.springframework.web.reactive.socket.WebSocketSession

data class Connection(val id: String, val session: WebSocketSession)
