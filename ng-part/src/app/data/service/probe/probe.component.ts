import {Component} from "@angular/core"
import {WebsocketService} from "../websocket.service"

@Component({
  selector: "app-probe",
  templateUrl: "./probe.component.html",
  styleUrls: [ "./probe.component.scss" ]
})
export class ProbeComponent {
  receivedMessages: string[] = []

  constructor(private websocketService: WebsocketService) {
    this.websocketService.connect()

    this.websocketService.messageReceived.subscribe((message: string) => {
      this.receivedMessages.push(message)
    })
  }

  sendMessage(): void {
    const message = "Hello, WebSocket!"
    this.websocketService.sendMessage(message)
  }
}
