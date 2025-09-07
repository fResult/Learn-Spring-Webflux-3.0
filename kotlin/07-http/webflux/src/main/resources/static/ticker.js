addEventListener('load', onLoad)

/**
 * @param ev { Event }
 */
function onLoad(ev) {
  const tickTockElement = document.querySelector('#update-block')
  const eventSource = new EventSource('http://localhost:8080/ticker-stream')

  eventSource.addEventListener('message', onMessage(tickTockElement))
}

/**
 * @param element { HTMLElement }
 * @returns { (ev: MessageEvent<string>) => void }
 */
function onMessage(element) {
  return ev => element.innerHTML = ev.data
}
