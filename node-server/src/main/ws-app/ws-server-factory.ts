/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import WebSocket from "ws"
import {wsMessageEventOn} from "@main/ws-app/event-hendler"
import {Constant} from "@main/Constant"
import {createServer, Server as HTTPServer} from "http"

export class WsServerFactory {
  private constructor() {
  }

  static start(port?: number, starterActions?: (ws: WebSocket) => void): WebSocket.Server {
    const httpServer = WsServerFactory.checkPortAndAndCreateHttpServer(port)

    return WsServerFactory.startServer(httpServer, starterActions)
  }

  static startServer(server: HTTPServer, starterActions?: (ws: WebSocket) => void): WebSocket.Server {
    // const wsServer = new WebSocket.Server({server})
    const wsServer = new WebSocket.Server({server})

    WsServerFactory.defineWsServerOn(wsServer, starterActions)

    return wsServer
  }

  private static checkPortAndAndCreateHttpServer(port?: number): HTTPServer {
    if (!port) {
      port = (process.env.WEBSOCKET_PORT || Constant.DEFAULT_WS_SERVER_PORT) as number
    }

    return createServer().listen(
      port,
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
