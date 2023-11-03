import {Component} from "@angular/core"

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: [ "./app.component.scss" ]
})
export class AppComponent {
  title = "ng-part"

  isOpen: boolean = false //todo: is necessary
  dropdownOpen: boolean = false
}
