const WebSocket = require('ws')
const wss = new WebSocket.Server({port: 5000}, () => console.log("started on port 5000"))

wss.on('connection', ws => {
  console.log("websocket entered")
  // onConnection(ws)
  ws.on('message', message => {
    // onMessage(message, ws)
    console.log("websocket message: " + message)
  })
  ws.on('error', error => {
    // OnError(error)
    console.log("websocket error")
  })
  ws.on('close', ws => {
    // onClose()
    console.log("websocket close")
  })
})
