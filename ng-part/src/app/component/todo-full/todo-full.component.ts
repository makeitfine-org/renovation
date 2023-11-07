import {Component} from "@angular/core"
import {TodoCrudService} from "src/app/data/service/todo-crud.service"
import {Todo} from "src/app/data/model/todo.model"
import {delay, retry} from "rxjs/operators"
import {ModalService} from "src/app/data/service/modal.service"

@Component({
  selector: "app-todo-full",
  templateUrl: "./todo-full.component.html",
  styleUrls: [ "./todo-full.component.scss" ]
})
export class TodoFullComponent {
  todos: Todo[] = []
  loading = true
  term = ""

  constructor(public todoCrudService: TodoCrudService, public modalService: ModalService) {
    this.todoCrudService.getTodos()
      .pipe(
        retry(2),
        delay(750)
      )
      .subscribe(todos => {
        this.todos = todos
        this.loading = false
      })
  }
}
