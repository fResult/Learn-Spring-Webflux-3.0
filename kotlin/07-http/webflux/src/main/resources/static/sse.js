/**
 * @param msg { string }
 */
function log(msg) {
  const messages = document.querySelector('#messages')
  const elem = document.createElement('div')
  const messageText = document.createTextNode(msg)
  elem.appendChild(messageText)
  messages.append(elem)
}

addEventListener('load', function onLoad(ev) {
  console.log("window has loaded.")
  const eventSource = new EventSource('http://localhost:8080/sse/10')
  const onError = parseEventSource(eventSource)

  eventSource.addEventListener('message', onMessage)
  eventSource.addEventListener('error', onError)
})

/** @param ev { MessageEvent<string> } */
function onMessage(ev) {
  console.log('message received', ev.data)
  ev.preventDefault()
  log(ev.data)
}

/**
 * @param eventSource { EventSource }
 * @returns { function(Event): void }
 */
function parseEventSource(eventSource) {
  return function onError(ev) {
    ev.preventDefault()
    log('closing the EventSource...')
    eventSource.close()
  }
}
