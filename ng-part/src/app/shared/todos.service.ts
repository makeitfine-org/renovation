import {Injectable} from '@angular/core'
import {tap} from 'rxjs/operators'
import {TodosCrudService} from "./todos-crud.service"
import {Todo} from "./todos.common"

export {Todo} from "./todos.common"

@Injectable({providedIn: 'root'})
export class TodosService {
  public todos: Todo[] = []

  constructor(private crudTodosService: TodosCrudService) {
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
        // this.todos.push(todo)
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
