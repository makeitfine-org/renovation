import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../environments/environment";
import {DatePipe} from "@angular/common";
import {Constants, Todo} from "./todos.common";

@Injectable({providedIn: 'root'})
export class CrudTodosService {
  constructor(private http: HttpClient, private datePipe: DatePipe) {
  }

  getTodos() {
    return this.http.get<Todo[]>(environment.v1ApiTodoUrl)
  }

  deleteTodo(id: number) {
    return this.http.delete(environment.v1ApiTodoUrl + '/' + id);
  }

  createTodo(todo: Todo): Observable<Object> {
    const todoForSave = {
      ...todo,
      date: this.datePipe.transform(todo.date, Constants.TIME_DATE_FORMAT)
    }
    return this.http.post(environment.v1ApiTodoUrl, todoForSave);
  }
}
