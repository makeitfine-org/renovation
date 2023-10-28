import {Injectable} from '@angular/core'
import {HttpClient} from '@angular/common/http'
import {environment} from "../../environments/environment"
import {DatePipe} from "@angular/common"
import {Constants, Todo} from "./todos.common"

@Injectable({providedIn: 'root'})
export class TodosCrudService {
  constructor(private http: HttpClient, private datePipe: DatePipe) {
  }

  getTodos() {
    return this.http.get<Todo[]>(environment.v1ApiTodoUrl)
  }

  deleteTodo(id: number) {
    return this.http.delete(environment.v1ApiTodoUrl + '/' + id)
  }

  createTodo = (todo: Todo) => this.http.post(environment.v1ApiTodoUrl, this.dateModifiedTodo(todo))

  updateTodo = (todo: Todo) => this.http.put(environment.v1ApiTodoUrl, this.dateModifiedTodo(todo))

  private dateModifiedTodo = (todo: Todo) => {
    return {
      ...todo,
      date:
        this.datePipe.transform(todo.date, Constants.TIME_DATE_FORMAT)
    }
  }
}
