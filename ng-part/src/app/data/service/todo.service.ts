/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Injectable} from '@angular/core'
import {tap} from 'rxjs/operators'
import {TodoCrudService} from "./todos-crud.service"

import {Todo} from "src/app/data/model/todo.model"

@Injectable({providedIn: 'root'})
export class TodoService {
  public todos: Todo[] = []

  constructor(private crudTodosService: TodoCrudService) {
  }

  fetchTodos() {
    return this.crudTodosService.getTodos()
      .pipe(tap(todos => this.todos = todos))
  }

  toggleCompletedFlag(id: number) {
    const todo = this.todos.find(t => t.id == id)!
    todo.completed = !todo.completed

    this.crudTodosService.updateTodo(todo)
      .subscribe(() => {
        // this.todo.push(todo)
      })
  }

  removeTodo(id: number) {
    this.crudTodosService.deleteTodo(id)
      .subscribe(() => {
        this.todos = this.todos.filter(t => t.id !== id)
      })
  }

  addTodo(todo: Todo) {
    this.crudTodosService.createTodo(todo)
      .subscribe(() => {
        this.todos.push(todo)
      })
  }
}
