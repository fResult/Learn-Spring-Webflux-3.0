package com.fResult.http.views

import com.fResult.utils.IntervalMessageProducer
import kotlinx.coroutines.reactive.asFlow
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable

@Controller
class TickerSseController {
  @GetMapping("/ticker.php")
  fun initialView() = "ticker"

  @GetMapping("/ticker-stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun streamingUpdates(model: Model): String {
    val updates = ReactiveDataDriverContextVariable(IntervalMessageProducer.produce().asFlow(), 1)
    model.addAttribute("updates", updates)

    return "ticker :: #update-block"
  }
}
