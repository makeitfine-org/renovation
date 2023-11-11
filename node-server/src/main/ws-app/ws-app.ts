/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {PhraseService} from "@main/data/service/phrase.service"
import WebSocket from "ws"

const phraseService = PhraseService.getInstance()
const texts = [ "Text Data" ]
let counter = 0

export const wsServerOn = (ws:WebSocket) => {
  ws.binaryType = "arraybuffer"

  console.log("WebSocket connection!")

  ws.on("message", message => {
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
    event: "messages", message: phraseService.getPhrase()
  }))

  ws.send(JSON.stringify({
    event: "update-texts", buffer: Buffer.from(JSON.stringify(texts))
  }))

  const timer = () => {
    ws.send(JSON.stringify({
      event: "counter", counter: ++counter
    }))
  }

  const interval = setInterval(timer, 10000)

  ws.on("close", () => {
    console.log("disconnected")
    clearInterval(interval)
  })
}
