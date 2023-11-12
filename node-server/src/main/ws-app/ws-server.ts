/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import WebSocket from "ws"
import {PhraseService} from "@main/data/service/phrase.service"
import {WsServerFactory} from "@main/ws-app/ws-server-factory"

const serverStartConnectionActions = (ws: WebSocket) => {
  ws.send(JSON.stringify({
    event: "messages", message: PhraseService.getInstance().getPhrase()
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

WsServerFactory.start(undefined, serverStartConnectionActions)
