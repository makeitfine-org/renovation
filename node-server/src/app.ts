import WebSocket from "ws"

const wsServer = new WebSocket.Server({port: 3000}, () => console.log("started on port 6759"))

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

wsServer.on('connection', (ws) => {
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

// import express from "express"
// import http from "http"
// import WebSocket from "ws"
//
// const app = express()
// const port = process.env.PORT || 3000
//
// const server = http.createServer(app)
// const webSocket = new WebSocket.Server({server})
//
// webSocket.on("connection", (ws: WebSocket) => {
//   console.log("New client connected")
//
//   // Log whenever a client disconnects from our websocket server
//   ws.on("disconnect", function () {
//     console.log("user disconnected")
//   })
//
//   ws.on("message", (message: string) => {
//     console.log(`Received message: ${ message }`)
//     // ws.send(`Server received your message: ${ message }`)
//   })
//
//   ws.on("close", () => {
//     console.log("Client disconnected")
//   })
// })
