import {Component} from "@angular/core"
import {TodoCrudService} from "../../data/service/todo-crud.service"
import {Todo} from "../../data/model/todo.model"
import {delay} from "rxjs/operators"

@Component({
  selector: "app-todo-full",
  templateUrl: "./todo-full.component.html",
  styleUrls: [ "./todo-full.component.scss" ]
})
export class TodoFullComponent {
  todos: Todo[] = []
  loading = true
  term = ""

  constructor(public todoCrudService: TodoCrudService) {
    this.todoCrudService.getTodos()
      .pipe(delay(750))
      .subscribe(() => {
      this.loading = false
    })
  }
}
