/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

const WebSocketServer = require('ws').Server
const wss = new WebSocketServer({port: 6759}, () => console.log("started on port 6759"))

const messages = [{
  id: 1, text: 'Hello everyone!!!'
}, {
  id: 2, text: 'I\'m here!!!'
}, {
  id: 3, text: 'Who there???'
}, {
  id: 4, text: 'Damn!'
}, {
  id: 5, text: 'I\'m off'
}]
const texts = ['Text Data']
let counter = 0


wss.on('connection', (ws) => {
  ws.binaryType = 'arraybuffer'

  console.log('WebSocket connection!')

  ws.on('message', message => {
    // onMessage(message, ws)
    console.log("websocket message: " + message)
  })

  // ws.on('message', (event) => {
  //   const message = JSON.parse(event)
  //
  //   switch (message.event) {
  //     case 'set-text':
  //       texts.unshift(message.data)
  //
  //       break
  //     case 'remove-text':
  //       texts.splice(message.data, 1)
  //       break
  //   }
  //
  //   ws.send(JSON.stringify({
  //     event: 'update-texts', buffer: Buffer.from(JSON.stringify(texts))
  //   }))
  //
  //   console.log('message:>', message)
  // })

  ws.send(JSON.stringify({
    event: 'messages', message: messages
  }))

  ws.send(JSON.stringify({
    event: 'update-texts', buffer: Buffer.from(JSON.stringify(texts))
  }))

  const timer = () => {
    ws.send(JSON.stringify({
      event: 'counter-abc', counter: ++counter
    }))
  }

  const interval = setInterval(timer, 10000)

  ws.on('close', () => {
    console.log('disconnected')
    clearInterval(interval)
  })

})
