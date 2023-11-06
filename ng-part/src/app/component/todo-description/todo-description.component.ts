import {Component, Input} from "@angular/core"
import {Todo} from "../../data/model/todo.model"

@Component({
    selector: "app-todo-description",
    templateUrl: "./todo-description.component.html",
    styleUrls: [ "./todo-description.component.scss" ]
})
export class TodoDescriptionComponent {
    @Input() todo?: Todo

    details = false
}
