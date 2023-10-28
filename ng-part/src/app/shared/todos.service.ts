import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {environment} from "../../environments/environment";
import {DatePipe} from "@angular/common";

export interface Todo {
  id: number
  title: string
  completed: boolean
  date?: Date
}

@Injectable({providedIn: 'root'})
export class TodosService {
  public todos: Todo[] = []
  public TIME_DATE_FORMAT = "yyyy-MM-dd HH:mm"

  constructor(private http: HttpClient, private datePipe: DatePipe) {
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

  addTodo(todo: Todo) {
    this.createTodo(todo)
      .subscribe(response => {
        this.todos.push(todo)
      });
  }

  private deleteTodo(id: number) {
    return this.http.delete(environment.v1ApiTodoUrl + '/' + id);
  }

  private createTodo(todo: Todo): Observable<Object> {
    const todoForSave = {
      ...todo,
      date: this.datePipe.transform(todo.date, this.TIME_DATE_FORMAT)
    }
    return this.http.post(environment.v1ApiTodoUrl, todoForSave);
  }
}
