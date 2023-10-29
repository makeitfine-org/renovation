import {Component, OnInit} from '@angular/core'
import {Todo, TodosService} from '../shared/todos.service'

@Component({
  selector: 'app-todo-form',
  templateUrl: './todos-form.component.html',
  styleUrls: ['./todos-form.component.scss']
})
export class TodosFormComponent {

  title: string = ''

  constructor(private todosService: TodosService) {
  }

  addTodo() {
    const todo: Todo = {
      title: this.title,
      id: this.getRandomInt(0, 1000_000),
      completed: false,
      date: new Date()
    }

    this.todosService.addTodo(todo)
    this.title = ''
  }

  private getRandomInt(min: number, max: number) {
    min = Math.ceil(min)
    max = Math.floor(max)
    return Math.floor(Math.random() * (max - min + 1)) + min
  }
}
