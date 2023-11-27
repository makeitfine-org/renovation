import {Component} from "@angular/core"

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: [ "./app.component.scss" ]
})
export class AppComponent {
  title = ""

  /**
   * Created for test reasons
   */
  setTitle(): Promise<void> {
    return new Promise((res) => {
      setTimeout(() => {
        this.title = "ng-part"
        res()
      }, 100)
    })
  }
}
