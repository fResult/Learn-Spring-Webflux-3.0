addEventListener('load', onLoad)

/**
 * @param ev { Event }
 */
function onLoad(ev) {
  const messages = document.querySelector('#messages')
  const sendButton = document.querySelector('#send')
  const messageText = document.querySelector('#message')
  const webSocket = new WebSocket('ws://localhost:8080/ws/chat')

  webSocket.addEventListener('open', (_) => {
    console.log('[onOpen] WebSocket connection established')
    const welcomeDiv = document.createElement('div')
    welcomeDiv.innerText = 'WebSocket connection established'
    messages.appendChild(welcomeDiv)
  })

  webSocket.addEventListener('close', () => {
    console.log('[onClose] WebSocket connection closed')
    messages.innerHTML += '<div>Disconnected from chat server</div>'
  })

  webSocket.addEventListener('error', (error) => {
    console.error('[onError] WebSocket error:', error)
    messages.innerHTML += '<div>Connection error occurred</div>'
  })

  webSocket.addEventListener('message', onMessage(messages))
  messageText.addEventListener('keydown', onKeyEnterDown(webSocket, messageText))
  sendButton.addEventListener('click', onClick(webSocket, messageText))
}

/**
 * @param messages { HTMLElement }
 * @returns { (ev: MessageEvent<string>) => void}
 */
function onMessage(messages) {
  return ev => {
    ev.preventDefault()
    console.log('[onMessage] message received', ev.data)
    const element = document.createElement('div')
    element.innerText = ev.data
    messages.appendChild(element)
  }
}

/**
 * @param webSocket { WebSocket }
 * @param messageInput { HTMLInputElement }
 * @returns { void }
 */
function send(webSocket, messageInput) {
  if (webSocket.readyState !== WebSocket.OPEN) {
    console.error('Cannot send message, WebSocket is not OPEN', webSocket.readyState)
    return;
  }

  const msgValue = messageInput.value
  messageInput.value = ''
  webSocket.send(JSON.stringify({ text: msgValue.trim() }))
  console.log('[send] message sent:', msgValue.trim())
}

/**
 * @param webSocket { WebSocket }
 * @param messageInput { HTMLInputElement }
 * @returns { (ev: KeyboardEvent) => void }
 */
function onKeyEnterDown(webSocket, messageInput) {
  return (ev) => {
    if (ev.key === 'Enter') {
      send(webSocket, messageInput)
    }
  }
}

/**
 * @param webSocket { WebSocket }
 * @param messageInput { HTMLInputElement }
 * @returns { (ev: MouseEvent) => void }
 */
function onClick(webSocket, messageInput) {
  return (ev) => {
    send(webSocket, messageInput)
    ev.preventDefault()
  }
}
