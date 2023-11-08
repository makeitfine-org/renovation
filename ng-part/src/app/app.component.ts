import {Component} from "@angular/core"
import {WebsocketService} from "./data/service/websocket.service"

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: [ "./app.component.scss" ]
})
export class AppComponent {
  title = "ng-part"

  constructor(private websocketService: WebsocketService) {
    this.websocketService.connect()

    setTimeout(
      () => this.websocketService.sendMessage("go go go!!!"), 50
    )
  }
}
