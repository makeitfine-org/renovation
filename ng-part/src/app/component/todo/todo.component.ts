/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Component, OnInit} from "@angular/core"
import {TodoService} from "src/app/data/service/todo.service"
import {delay} from "rxjs/operators"
import {Constant} from "src/app/data/model/constant.modal"

@Component({
  selector: "app-todo",
  templateUrl: "./todo.component.html",
  styleUrls: [ "./todo.component.scss" ]
})
export class TodoComponent implements OnInit {

  loading: boolean = true
  searchString = ""

  constructor(public todoService: TodoService) {
  }

  ngOnInit() {
    this.todoService.fetchTodos()
      .pipe(delay(500))
      .subscribe(() => {
        this.loading = false
      })
  }

  onChange(id: number) {
    this.todoService.toggleCompletedFlag(id)
  }

  removeTodo(id: number) {
    this.todoService.removeTodo(id)
  }

  protected readonly TIME_DATE_FORMAT = Constant.TIME_DATE_FORMAT
}
