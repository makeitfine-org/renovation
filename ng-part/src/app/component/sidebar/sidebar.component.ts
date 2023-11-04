import {Component} from "@angular/core"
import {UiService} from "../../data/service/ui.service"

@Component({
  selector: "app-sidebar",
  templateUrl: "./sidebar.component.html",
  styleUrls: [ "./sidebar.component.scss" ]
})
export class SidebarComponent {

  readonly activeClass = "bg-gray-600 bg-opacity-25 text-gray-100 border-gray-100"

  readonly inactiveClass
    = "border-gray-900 text-gray-500 hover:bg-gray-600 hover:bg-opacity-25 hover:text-gray-100"

  constructor(public uiService: UiService) {
  }
}
