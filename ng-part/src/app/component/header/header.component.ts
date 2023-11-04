import {Component} from "@angular/core"
import {UiService} from "../../data/service/ui.service"

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: [ "./header.component.scss" ]
})
export class HeaderComponent {
  isDropdownOpen = false

  constructor(public uiService: UiService) {
  }

  toggleDropdownOpen = () => this.isDropdownOpen = !this.isDropdownOpen
}
