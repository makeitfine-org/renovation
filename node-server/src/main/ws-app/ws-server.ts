/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import WebSocket from "ws"
import {wsServerOn} from "@main/ws-app/ws-app"

const port = (process.env.WEBSOCKET_PORT || 6759) as number //todo: add env. var for port
export const wsServer = new WebSocket.Server(
  {port: port},
  () => console.log(`started websocket server on port ${ port }`)
)

wsServer.on("connection", wsServerOn)
