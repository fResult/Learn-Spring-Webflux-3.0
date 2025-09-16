package com.fResult.rsocket.bidirectional

class ClientHealthState(val state: String) {
  companion object {
    const val STARTED = "started"
    const val STOPPED = "stopped"
  }
}
