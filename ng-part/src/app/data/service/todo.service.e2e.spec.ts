/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {TestBed} from "@angular/core/testing"
import {HttpClientModule} from "@angular/common/http"
import {TodoService} from "./todo.service"
import {DatePipe} from "@angular/common"
import {TodoCrudService} from "./todo-crud.service"
import {ErrorService} from "./error.service"
import {convertTodoDateOfStringRepresentationToDateArray, expectedTodos} from "./test.utils"

describe("TodoService", () => {
  let todoService: TodoService

  // @ts-ignore
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  function dateFormatFromBE(date: Date) {
    const datePipe = TestBed.inject(DatePipe)

    return date.getSeconds() === 0
      ? datePipe.transform(date, `yyyy-MM-ddTHH:mm`)
      : datePipe.transform(date, `yyyy-MM-ddTHH:mm:ss`)
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientModule, ],
      providers: [ DatePipe, TodoCrudService, ErrorService ],
    })

    todoService = TestBed.inject(TodoService)
  })

  it("module", (done: DoneFn) => {
    todoService.fetchTodos().subscribe((todos) => {
      expect(5).toBe(todos.length)
      expect(expectedTodos).toEqual(convertTodoDateOfStringRepresentationToDateArray(todos))

      done()
    })
  })
})
