import {Component, OnInit} from "@angular/core"
import {TodoCrudService} from "src/app/data/service/todo-crud.service"
import {Todo} from "src/app/data/model/todo.model"
import {delay, retry} from "rxjs/operators"
import {ModalService} from "src/app/data/service/modal.service"
import {Constant} from "src/app/data/model/constant.modal"

@Component({
  selector: "app-todo-full",
  templateUrl: "./todo-full.component.html",
  styleUrls: [ "./todo-full.component.scss" ]
})
export class TodoFullComponent implements OnInit{
  todos: Todo[] = []
  loading = true
  term = ""

  constructor(public todoCrudService: TodoCrudService, public modalService: ModalService) {
  }

  ngOnInit(): void {
    this.todoCrudService.getTodos()
      .pipe(
        retry(2),
        delay(750)
      )
      .subscribe(todos => {
        this.todos = todos.map(e => {
          e.image = e.image ?? Constant.DEFAULT_TODO_IMG
          return e
        })
        this.loading = false
      })
  }
}
