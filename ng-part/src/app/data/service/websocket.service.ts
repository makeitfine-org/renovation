/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Injectable} from "@angular/core"
import {Subject} from "rxjs"
import {environment} from "src/environments/environment"

@Injectable({
  providedIn: "root"
})
export class WebsocketService {
  private socket: WebSocket | undefined
  messageReceived: Subject<string> = new Subject<string>()

  /* eslint-disable  @typescript-eslint/no-useless-constructor */
  constructor() {
  }

  connect(): void {
    this.socket = new WebSocket(environment.websocketServerUrl)

    this.socket.onopen = () => {
      console.log("WebSocket connection established.")
    }

    this.socket.onmessage = (event) => {
      const message = event.data
      console.log("Received message:", message)
      this.messageReceived.next(message)
    }

    this.socket.onclose = (event) => {
      console.log("WebSocket connection closed:", event)
    }

    this.socket.onerror = (error) => {
      console.error("WebSocket error:", error)
    }
  }

  sendMessage(message: string): void {
    // @ts-ignore
    this.socket.send(message)
  }

  closeConnection(): void {
    // @ts-ignore
    this.socket.close()
  }
}
