package com.fResult.rsocket.metadata

import org.springframework.util.MimeType

class Constants {
  companion object {
    const val CLIENT_ID_HEADER = "client-id"
    const val CLIENT_ID_VALUE = "messaging/x.fResult.$CLIENT_ID_HEADER"

    const val LANG_HEADER = "lang"
    const val LANG_VALUE = "messaging/x.fResult.$LANG_HEADER"

    val CLIENT_ID = MimeType.valueOf(CLIENT_ID_VALUE)
    val LANG = MimeType.valueOf(LANG_VALUE)
  }
}
