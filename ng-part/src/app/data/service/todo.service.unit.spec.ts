/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {TestBed} from "@angular/core/testing"
import {TodoService} from "./todo.service"
import {HttpClientTestingModule} from "@angular/common/http/testing"
import {TodoCrudService} from "./todo-crud.service"
import {DatePipe} from "@angular/common"
import {of} from "rxjs"
import {ErrorService} from "./error.service"
import {Todo} from "../model/todo.model"

describe("TodoService", () => {
  const testTodo = {id: 1, title: "title", completed: true}
  let todoService: TodoService
  let todoCrudService: jasmine.SpyObj<TodoCrudService>

  // const datePipe = jasmine.createSpyObj("DatePipe")
  const errorServiceSpy = jasmine.createSpyObj("ErrorService", [ "handle" ])
  const todoCrudServiceSpy = jasmine.createSpyObj("TodoCrudService",
    [ "getTodos", "updateTodo", "deleteTodo", "createTodo" ])


  beforeAll(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [
        TodoService,
        DatePipe,
        // {provider: DatePipe, useValue: datePipe},
        {provider: ErrorService, useValue: errorServiceSpy},
        {provide: TodoCrudService, useValue: todoCrudServiceSpy}
      ]
    })
    todoService = TestBed.inject(TodoService)
    todoCrudService = TestBed.inject(TodoCrudService) as jasmine.SpyObj<TodoCrudService>

    todoCrudService.getTodos.and.returnValue(of([ testTodo ] as Todo[]))
    todoCrudService.updateTodo.and.callFake((todo) => {
      if (JSON.stringify(todo) === JSON.stringify(testTodo)) {
        return of()
      } else {
        throw new Error("unexpected")
      }
    })
    todoCrudService.deleteTodo.and.callFake(id => {
      if (id === 1) {
        return of()
      } else {
        throw new Error("unexpected")
      }
    })
    todoCrudService.createTodo.and.returnValue(of())

  })

  it(`fetchTodos`, (done: DoneFn) => {
    todoService.fetchTodos().subscribe((todos) => {
      expect(todos).toEqual([ testTodo ])
      done()
    })
  })

  it(`toggleCompletedFlag`, () => {
    const completedBeforeToggle = testTodo.completed
    todoService.toggleCompletedFlag(1)
    expect(testTodo.completed).toBe(!completedBeforeToggle)
  })

  it(`removeTodo`, () => {
    todoService.removeTodo(1)
    expect(todoService.todos).toContain(testTodo)
  })

  it(`removeTodo`, () => {
    todoService.addTodo(testTodo)
    expect(todoService.todos).toContain(testTodo)
  })
})
