/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Injectable} from "@angular/core"
import {HttpClient} from "@angular/common/http"
import {environment} from "src/environments/environment"
import {DatePipe} from "@angular/common"
import {Constant} from "src/app/data/model/constant.modal"
import {Todo} from "src/app/data/model/todo.model"

@Injectable({providedIn: "root"})
export class TodoCrudService {
  constructor(private http: HttpClient, private datePipe: DatePipe) {
  }

  getTodos() {
    return this.http.get<Todo[]>(environment.v1ApiTodoUrl)
  }

  deleteTodo(id: number) {
    return this.http.delete(environment.v1ApiTodoUrl + "/" + id)
  }

  createTodo = (todo: Todo) => this.http.post(environment.v1ApiTodoUrl, this.dateModifiedTodo(todo))

  updateTodo = (todo: Todo) => this.http.put(environment.v1ApiTodoUrl, this.dateModifiedTodo(todo))

  private dateModifiedTodo = (todo: Todo) => {
    return {
      ...todo,
      date:
        this.datePipe.transform(todo.date, Constant.TIME_DATE_FORMAT)
    }
  }
}
