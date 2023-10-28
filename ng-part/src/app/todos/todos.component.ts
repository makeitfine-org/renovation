import {Component, OnInit} from '@angular/core'
import {TodosService} from '../shared/todos.service'
import {delay} from 'rxjs/operators'
import {Constants} from "../shared/todos.common"

@Component({
  selector: 'app-todos',
  templateUrl: './todos.component.html',
  styleUrls: ['./todos.component.scss']
})
export class TodosComponent implements OnInit {

  loading: boolean = true
  searchString = ''

  constructor(public todosService: TodosService) {
  }

  ngOnInit() {
    this.todosService.fetchTodos()
      .pipe(delay(500))
      .subscribe(() => {
        this.loading = false
      })
  }

  onChange(id: number) {
    this.todosService.toggleCompletedFlag(id)
  }

  removeTodo(id: number) {
    this.todosService.removeTodo(id)
  }

  protected readonly TIME_DATE_FORMAT = Constants.TIME_DATE_FORMAT
}
