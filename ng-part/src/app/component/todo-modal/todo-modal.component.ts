import {Component, HostListener, Input} from "@angular/core"
import {ModalService} from "src/app/data/service/modal.service"

@Component({
  selector: "app-todo-modal",
  templateUrl: "./todo-modal.component.html",
  styleUrls: [ "./todo-modal.component.scss" ]
})
export class TodoModalComponent {
  @Input() title: string = ""

  constructor(public modalService: ModalService) {
  }

  @HostListener("document:keydown.escape", [ "$event" ])
  onEscapeKeydownHandler() {
    if (this.modalService.isVisible$.value) {
      this.modalService.close()
    }
  }
}
