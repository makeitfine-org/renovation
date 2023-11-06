import {Component, HostListener} from "@angular/core"

@Component({
  selector: "app-modal",
  templateUrl: "./modal.component.html",
  styleUrls: [ "./modal.component.scss" ]
})
export class ModalComponent {
  isOpen = false

  @HostListener("document:keydown.escape", [ "$event" ])
  onEscapeKeydownHandler() {
    if (this.isOpen) {
      this.isOpen = false
    }
  }
}
