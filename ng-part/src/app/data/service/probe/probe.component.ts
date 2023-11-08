import {Component} from "@angular/core"
import {WebsocketService} from "../websocket.service"

@Component({
  selector: "app-probe",
  templateUrl: "./probe.component.html",
  styleUrls: [ "./probe.component.scss" ]
})
export class ProbeComponent {
  messages: string[] = []

  constructor(private websocketService: WebsocketService) {
    this.connectWebSocket()

    this.websocketService.messageReceivedFromWsServer.subscribe((message: string) => {
      this.messages.push(message)
    })
  }

  sendMessage(): void {
    const message = "Hello, WebSocket!"
    this.websocketService.sendMessage(message)
  }

  connectWebSocket(): void {
    if (!this.websocketService.isConnectionOpen()) {
      this.websocketService.connect()
    }
  }

  closeWebSocket(): void {
    this.websocketService.closeConnection()
  }

  // @ts-ignore
  private static WebSocketConnection = class {
    private isWebSocketConnected: boolean

    constructor(startState: boolean = false) {
      this.isWebSocketConnected = startState
    }

    open() {
      this.isWebSocketConnected = true
    }

    close() {
      this.isWebSocketConnected = true
    }

    status = () => this.isWebSocketConnected
  }
}
