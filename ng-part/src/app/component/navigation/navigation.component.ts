import {Component} from "@angular/core"

export type Img = {
  src: string,
  alt: string,
}

@Component({
  selector: "app-navigation",
  templateUrl: "./navigation.component.html",
  styleUrls: [ "./navigation.component.scss" ]
})
export class NavigationComponent {

  menuIcon: Img = {
    src: "assets/images/menuIcon.svg",
    alt: "Menu icon"
  }
}
