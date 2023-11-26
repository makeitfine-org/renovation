/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {TestBed} from "@angular/core/testing"
import {TodoService} from "./todo.service"
import {HttpClientTestingModule} from "@angular/common/http/testing"
import {TodoCrudService} from "./todo-crud.service"
import {BehaviorSubject, firstValueFrom, of, throwError} from "rxjs"
import {ErrorService} from "./error.service"
import {Todo} from "../model/todo.model"

describe("TodoService", () => {
  const testTodo = {id: 1, title: "title", completed: true}
  let todoService: TodoService
  let todoCrudService: jasmine.SpyObj<TodoCrudService>
  let errorService: jasmine.SpyObj<ErrorService>

  const errorServiceSpy = jasmine.createSpyObj("ErrorService", [ "handle" ])
  const todoCrudServiceSpy = jasmine.createSpyObj("TodoCrudService",
    [ "getTodos", "updateTodo", "deleteTodo", "createTodo" ])


  beforeAll(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [
        TodoService,
        {provide: ErrorService, useValue: errorServiceSpy},
        {provide: TodoCrudService, useValue: todoCrudServiceSpy}
      ]
    })
    todoService = TestBed.inject(TodoService)
    todoCrudService = TestBed.inject(TodoCrudService) as jasmine.SpyObj<TodoCrudService>
    errorService = TestBed.inject(ErrorService) as jasmine.SpyObj<ErrorService>

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

    errorService.handle.and.callFake(() => {
    })

    Object.defineProperty(errorService, "error$", {
      get: () => new BehaviorSubject<String>("!"), set: () => {
      }, configurable: true
    })
  })

  it(`just check error$`, () => {
    expect(errorService.error$).toBeTruthy()
    expect(errorService.error$).toEqual(new BehaviorSubject<String>("!"))
    expect(errorService.error$).not.toEqual(new BehaviorSubject<String>("!!"))
  })

  it(`fetchTodos with error`, (done: DoneFn) => {
    todoCrudService.getTodos.and.returnValue(throwError(() => new Error("Just error in getTodos")))

    // todoService.fetchTodos()
    // expect(()=>todoService.fetchTodos()).toThrowError("Just error in getTodos")
    // try{
    todoService.fetchTodos().subscribe({
      error: error => {
        expect(error).toBeTruthy()
        expect(error).toEqual("Just error in getTodos")
      }
    })
    expect(errorService.handle).toHaveBeenCalledTimes(1)

    todoCrudService.getTodos.and.returnValue(of([ testTodo ] as Todo[]))
    done()
  })

  it(`fetchTodos`, (done: DoneFn) => {
    todoCrudService.getTodos.calls.reset() //unnecessary

    todoService.fetchTodos().subscribe((todos) => {
      expect(todos).toEqual([ testTodo ])

      expect(todoCrudService.getTodos).toHaveBeenCalledTimes(1)
      expect(todoCrudService.getTodos).toHaveBeenCalledWith()
      done()
    })
  })

  it(`fetchTodos (async example)`, async() => {
    todoCrudService.getTodos.calls.reset() //unnecessary

    const todos = await firstValueFrom(todoService.fetchTodos())
    expect(todos).toEqual([ testTodo ])
    expect(todoCrudService.getTodos).toHaveBeenCalledTimes(1)
    expect(todoCrudService.getTodos).toHaveBeenCalledWith()
  })

  it(`fetchTodos (promise example)`, () => {
    todoCrudService.getTodos.calls.reset() //unnecessary

    return firstValueFrom(todoService.fetchTodos()).then((todos) => {
      expect(todos).toEqual([ testTodo ])

      expect(todoCrudService.getTodos).toHaveBeenCalledTimes(1)
      expect(todoCrudService.getTodos).toHaveBeenCalledWith()
    })
  })

  it(`toggleCompletedFlag`, () => {
    const completedBeforeToggle = testTodo.completed
    todoService.toggleCompletedFlag(1)
    expect(testTodo.completed).toBe(!completedBeforeToggle)

    expect(todoCrudService.updateTodo).toHaveBeenCalledTimes(1)
    expect(todoCrudService.updateTodo).toHaveBeenCalledWith(testTodo)
  })

  it(`removeTodo`, () => {
    todoService.removeTodo(1)
    expect(todoService.todos).toContain(testTodo)

    expect(todoCrudService.deleteTodo).toHaveBeenCalledTimes(1)
    expect(todoCrudService.deleteTodo).toHaveBeenCalledWith(1)
  })

  it(`addTodo`, () => {
    todoService.addTodo(testTodo)
    expect(todoService.todos).toContain(testTodo)

    expect(todoCrudService.createTodo).toHaveBeenCalledTimes(1)
    expect(todoCrudService.createTodo).toHaveBeenCalledWith(testTodo)
  })
})
