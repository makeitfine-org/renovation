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
    this.websocketService.connect()

    this.websocketService.messageReceivedFromWsServer.subscribe((message: string) => {
      this.messages.push(message)
    })
  }

  sendMessage(): void {
    const message = "Hello, WebSocket!"
    this.websocketService.sendMessage(message)
  }

  closeWebSocket(): void {
    this.websocketService.closeConnection()
  }
}
