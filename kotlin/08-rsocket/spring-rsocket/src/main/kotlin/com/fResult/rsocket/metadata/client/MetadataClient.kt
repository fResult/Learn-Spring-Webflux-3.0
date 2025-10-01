package com.fResult.rsocket.metadata.client

import com.fResult.rsocket.metadata.Constants
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Component
import java.util.*

@Component
class MetadataClient(private val requester: RSocketRequester) {
  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    val chinese = requester.route("message")
      .metadata(UUID.randomUUID().toString(), Constants.CLIENT_ID)
      .metadata(Locale.CHINESE.language, Constants.LANG)
      .send()

    val english = requester.route("message")
      .metadata(UUID.randomUUID().toString(), Constants.CLIENT_ID)
      .metadata(Locale.ENGLISH.language, Constants.LANG)
      .send()

    chinese.then(english).subscribe()
  }
}
