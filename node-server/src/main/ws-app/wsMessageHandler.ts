/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import WebSocket from "ws"

export const wsMessageHandler = (message: WebSocket.RawData) => {
  // onMessage(message, ws)
  console.log("websocket message: " + message)

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
}
