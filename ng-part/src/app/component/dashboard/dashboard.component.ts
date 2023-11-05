import {Component, OnInit} from "@angular/core"
import {Todo} from "../../data/model/todo.model"
import {TodoCrudService} from "src/app/data/service/todo-crud.service"
import {delay} from "rxjs/operators"
import {Constant} from "../../data/model/constant.modal"

export interface User {
  name: string
  email: string
  title: string
  title2: string
  status: string
  role: string
}

@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: [ "./dashboard.component.scss" ]
})
export class DashboardComponent implements OnInit {
  todos: Todo[]

  protected readonly TIME_DATE_FORMAT = Constant.TIME_DATE_FORMAT

  constructor(public todoCrudService: TodoCrudService) {
  }

  ngOnInit(): void {
    this.todoCrudService.getTodos()
      .pipe(delay(500))
      .subscribe(todos => {
        this.todos = todos
      })
  }

  completedTodo = () => this.todos.filter(e => e.completed).length

  uncompletedTodo = () => this.todos.filter(e => !e.completed).length

  protected readonly length = length
}
