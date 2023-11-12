/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import WebSocket from "ws"
import {wsMessageEventOn} from "@main/ws-app/event-hendler"

export class WsServerFactory {
  private constructor() {
  }

  static start(port?: number, starterActions?: (ws: WebSocket) => void): WebSocket.Server {
    const wsServer = WsServerFactory.checkPortAndAndCreateWsServer(port)

    WsServerFactory.defineWsServerOn(wsServer, starterActions)

    return wsServer
  }

  private static checkPortAndAndCreateWsServer(port?: number): WebSocket.Server {
    if (!port) {
      port = (process.env.WEBSOCKET_PORT || 6759) as number
    }

    return new WebSocket.Server(
      {port: port},
      () => console.log(`started websocket server on port ${ port }`)
    )
  }

  private static defineWsServerOn(wsServer: WebSocket.Server, starterActions?: (ws: WebSocket) => void) {
    let connectionCount = 0

    wsServer.on("connection", (ws: WebSocket) => {
      ws.binaryType = "arraybuffer"

      console.log(`WebSocket connection (num: ${ ++connectionCount })!`)

      ws.on("message", (rawData) => wsMessageEventOn(rawData, ws))

      ws.on("close", () => {
        console.log("disconnected")
      })

      ws.on("error", () => {
        console.log("open connection")
      })

      if (starterActions) {
        starterActions(ws)
      }
    })
  }
}
