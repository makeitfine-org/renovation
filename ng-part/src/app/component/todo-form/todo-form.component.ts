/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Component} from "@angular/core"
import {TodoService} from "src/app/data/service/todo.service"
import {Todo} from "src/app/data/model/todo.model"
import {Util} from "src/app/util/util"

@Component({
  selector: "app-todo-form",
  templateUrl: "./todo-form.component.html",
  styleUrls: [ "./todo-form.component.scss" ]
})
export class TodoFormComponent {

  title: string = ""

  constructor(private todoService: TodoService) {
  }

  addTodo() {
    const todo: Todo = {
      title: this.title,
      id: Util.getRandomInt(0, 1000_000),
      completed: false,
      date: new Date()
    }

    this.todoService.addTodo(todo)
    this.title = ""
  }
}
