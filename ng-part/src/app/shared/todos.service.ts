import {Injectable} from '@angular/core';
import {tap} from 'rxjs/operators';
import {CrudTodosService} from "./crud-todos.service";
import {Todo} from "./todos.common";

export {Todo} from "./todos.common";

@Injectable({providedIn: 'root'})
export class TodosService {
  public todos: Todo[] = []

  constructor(private crudTodosService: CrudTodosService) {
  }

  fetchTodos() {
    return this.crudTodosService.getTodos()
      .pipe(tap(todos => this.todos = todos))
  }

  toggleCompletedFlag(id: number) {
    const elem = this.todos.find(t => t.id == id)!
    elem.completed = !elem.completed
  }

  removeTodo(id: number) {
    this.crudTodosService.deleteTodo(id)
      .subscribe(response => {
        this.todos = this.todos.filter(t => t.id !== id)
      });
  }

  addTodo(todo: Todo) {
    this.crudTodosService.createTodo(todo)
      .subscribe(response => {
        this.todos.push(todo)
      });
  }
}
