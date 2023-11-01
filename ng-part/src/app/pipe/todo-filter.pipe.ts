/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Pipe, PipeTransform} from '@angular/core'
import {Todo} from "src/app/data/model/todo.model"

@Pipe({
  name: 'todoTitleFilter'
})
export class TodoTitleFilterPipe implements PipeTransform {
  transform(todos: Todo[], search: string = ''): Todo[] {
    if (!search.trim()) {
      return todos
    }

    return todos.filter(todo => todo.title.toLowerCase().includes(search.toLowerCase()))
  }
}
