/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import WebSocket from "ws"
import {wsMessageEventOn} from "@main/ws-app/event-hendler"
import {PhraseService} from "@main/data/service/phrase.service"

const phraseService = PhraseService.getInstance()

const port = (process.env.WEBSOCKET_PORT || 6759) as number //todo: add env. var for port
export const wsServer = new WebSocket.Server(
  {port: port},
  () => console.log(`started websocket server on port ${ port }`)
)

wsServer.on("connection", (ws: WebSocket) => {
  ws.binaryType = "arraybuffer"

  console.log("WebSocket connection!")

  ws.on("message", wsMessageEventOn)

  ws.on("close", () => {
    console.log("disconnected")
  })

  ws.on("error", () => {
    console.log("open connection")
  })

  serverStartConnectionActions(ws)
})

const serverStartConnectionActions = (ws: WebSocket) => {
  ws.send(JSON.stringify({
    event: "messages", message: phraseService.getPhrase()
  }))

  ws.send(JSON.stringify({
    event: "update-texts", buffer: Buffer.from(JSON.stringify([ "Text Data" ]))
  }))

  setInterval(() => {
    ws.send(JSON.stringify({
      event: "date and time", time: new Date().toTimeString()
    }))
  }, 10000)
}
