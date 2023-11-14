/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Injectable} from "@angular/core"
import {Subject} from "rxjs"
import {environment} from "src/environments/environment"
import {Constant} from "../model/constant.modal"

@Injectable({
  providedIn: "root"
})
export class WebsocketService {
  private socket: WebSocket | undefined
  messageReceivedFromWsServer: Subject<string> = new Subject<string>()

  /* eslint-disable  @typescript-eslint/no-useless-constructor */
  constructor() {
  }

  connect(): void {
    this.socket = new WebSocket(environment.websocketServerUrl)

    this.socket.onopen = () => {
      console.log("WebSocket connection established.")
    }

    this.socket.onmessage = (messageEvent) => {
      const message = messageEvent.data
      console.log(`Received message: ${ message }`)
      this.messageReceivedFromWsServer.next(message)
    }

    this.socket.onclose = (closeEvent: CloseEvent) => {
      console.log("WebSocket connection closed:", closeEvent)

      if (this.checkForIntentionalWebSocketClosing(closeEvent)) {
        return
      }

      console.log("Socket is closed. Reconnect will be attempted in 1 second.", closeEvent.reason)
      this.webSocketReconnect(closeEvent)
    }

    this.socket.onerror = (error) => {
      console.error("WebSocket error:", error)
      // @ts-ignore
      this.socket.close()
    }
  }

  sendMessage(message: string): void {
    // @ts-ignore
    this.socket.send(message)
  }

  closeConnection(): void {
    // @ts-ignore
    this.socket.close(Constant.WS_INTENTIONAL_CLOSE_CODE, Constant.WS_INTENTIONAL_CLOSE_MESSAGE)
  }

  isConnectionOpen = () => this.socket !== undefined && this.socket?.readyState !== WebSocket.CLOSED

  private webSocketReconnect = (closeEvent: CloseEvent) => {
    setTimeout(() => {
      console.log("Socket is closed. Reconnect will be attempted in 1 second.", closeEvent.reason)
      this.connect()
    }, 1000)
  }

  private checkForIntentionalWebSocketClosing = (closeEvent: CloseEvent) =>
    closeEvent.code === Constant.WS_INTENTIONAL_CLOSE_CODE
    && closeEvent.reason === Constant.WS_INTENTIONAL_CLOSE_MESSAGE
}
