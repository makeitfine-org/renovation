import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {environment} from "../../environments/environment";

export interface Todo {
  id: number
  title: string
  completed: boolean
  date?: any
}

@Injectable({providedIn: 'root'})
export class TodosService {
  public todos: Todo[] = []

  constructor(private http: HttpClient) {
  }

  fetchTodos(): Observable<Todo[]> {
    return this.http.get<Todo[]>(environment.v1ApiTodoUrl)
      .pipe(tap(todos => this.todos = todos))
  }

  toggleCompletedFlag(id: number) {
    const elem = this.todos.find(t => t.id == id)!
    elem.completed = !elem.completed
  }

  removeTodo(id: number) {
    this.deleteTodo(id)
      .subscribe(response => {
        this.todos = this.todos.filter(t => t.id !== id)
      });
  }

  deleteTodo(id: number) {
    return this.http.delete(environment.v1ApiTodoUrl + '/' + id);
  }

  // deleteTodo(id: number): Observable<any> {
  //   return this.http.delete(`${ environment.v1ApiTodoUrl }/${ id }`, {responseType: 'text'});
  // }

  addTodo(todo: Todo) {
    this.todos.push(todo)
  }
}
