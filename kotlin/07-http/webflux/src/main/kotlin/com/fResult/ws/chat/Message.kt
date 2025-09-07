package com.fResult.ws.chat

import java.time.Instant

data class Message(val clientID: String?, val text: String, val `when`: Instant?)
