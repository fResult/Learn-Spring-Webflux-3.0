/**
 * @param msg { string}
 * @returns { void }
 */
function log(msg) {
  const messages = document.querySelector('#messages')
  const messageElem = document.createElement('div')
  const messageText = document.createTextNode(msg)
  messageElem.appendChild(messageText)
  messages.append(messageElem)
}

/** @type { WebSocket } */
let webSocket = null

document.querySelector('#close')
  .addEventListener('click', onClickClose)

addEventListener('load', onPageLoad)

/**
 * @param { MouseEvent } ev
 * @return { boolean }
 */
function onClickClose(ev) {
  console.log('closing the WebSocket...')
  webSocket.close()
  return false
}

/**
 * @param ev { Event }
 */
function onPageLoad(ev) {
  webSocket = new WebSocket('ws://localhost:8080/ws/echo')
  webSocket.addEventListener('message', onMessage)
}

/**
 * @param ev { MessageEvent<string> }
 */
function onMessage(ev) {
  const msg = ev.data
  log(msg)
  webSocket.send(`${msg} reply`)
}
