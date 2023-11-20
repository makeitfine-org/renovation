/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {HttpClient, HttpClientModule, HttpErrorResponse} from "@angular/common/http"
import {TodoCrudService} from "./todo-crud.service"
import {DatePipe} from "@angular/common"
import {defer, firstValueFrom} from "rxjs"
import {Todo} from "../model/todo.model"
import {TestBed} from "@angular/core/testing"
import {convertTodoDateOfStringRepresentationToDateArray, expectedTodos} from "./test.utils"

describe("TodoCrudService", () => {
  let httpClientSpy: jasmine.SpyObj<HttpClient>
  let todoCrudService: TodoCrudService
  const datePipe = new DatePipe("en")

  const TODOS: Todo[] = [
    {id: 1, title: "I was born", completed: true},
    {id: 2, title: "born Was", completed: true},
    {id: 3, title: "quest it one", completed: false},
    {id: 4, title: "born wAs", completed: true},
    {id: 1, title: "Quest it one", completed: true},
    {id: 1, title: "Quest it one again", completed: true},
    {id: 1, title: "born was", completed: true}
  ]

  beforeEach(() => {
    // TODO: spy on other methods too
    httpClientSpy = jasmine.createSpyObj("HttpClient", [ "get" ])
    todoCrudService = new TodoCrudService(httpClientSpy, datePipe)
  })

  it("get todos", (done: DoneFn) => {
    httpClientSpy.get.and.returnValue(asyncData([ {id: 1, title: "t", completed: true} ]))

    todoCrudService.getTodos().subscribe({
      next: (todos) => {
        expect(todos).withContext("expected todos").toEqual([ {id: 1, title: "t", completed: true} ])
        done()
      },
      error: done.fail,
    })

    expect(httpClientSpy.get.calls.count()).withContext("one call").toBe(1)
    expect(TODOS.length).toBe(7) //todo: temp
  })

  it("should return an error when the server returns a 404", (done: DoneFn) => {
    const errorResponse = new HttpErrorResponse({
      error: "test 404 error",
      status: 404,
      statusText: "Not Found",
    })

    httpClientSpy.get.and.returnValue(asyncError(errorResponse))

    todoCrudService.getTodos().subscribe({
      next: () => done.fail("expected an error, not todos"),
      error: (error) => {
        expect(error.message).toContain("Http failure response for (unknown url): 404 Not Found")
        done()
      },
    })
  })

  function asyncData<T>(data: T) {
    return defer(() => Promise.resolve(data))
  }

  function asyncError(errorObject: any) {
    return defer(() => Promise.reject(errorObject))
  }
})

describe("TodoCrudService (real request to server)", () => {
  let todoCrudService: TodoCrudService
  const todoCreatedId = 6
  const todoCreated: Todo = {id: 6, title: "new title 6", completed: false}
  const todoUpdated: Todo = {id: 6, title: "new updated 6", completed: true}
  const todoDeletedId = 6


  beforeEach(() => {
    TestBed.configureTestingModule({
      // Import the HttpClient mocking services
      imports: [ HttpClientModule, ],
      // Provide the service-under-test
      providers: [ DatePipe ],
    })

    // Inject the http, test controller, and service-under-test
    // as they will be referenced by each test.
    todoCrudService = TestBed.inject(TodoCrudService)
  })

  it("todoCrudService.getTodos(): get todos: http://localhost:9190/api/v1/info/todo", async() => {
    const todos = await firstValueFrom(todoCrudService.getTodos())

    expect(5).toBe(todos.length)
    expect(expectedTodos).toEqual(convertTodoDateOfStringRepresentationToDateArray(todos))
  })

  it("todoCrudService.createTodo()", async() => {
    expect(5).toBe((await firstValueFrom(todoCrudService.getTodos())).length)

    await firstValueFrom(todoCrudService.createTodo(todoCreated)).then(
      async() => {
        const todos = await firstValueFrom(todoCrudService.getTodos())
        expect(6).toBe(todos.length)

        const foundCreated = todos.find(t => t.id == todoCreatedId)!!
        expect(foundCreated.id).toEqual(todoCreated.id)
        expect(foundCreated.title).toEqual(todoCreated.title)
        expect(foundCreated.completed).toEqual(todoCreated.completed)
      }
    )
  })

  it("todoCrudService.updateTodo()", async() => {
    const todoBeforeUpdate = await firstValueFrom(todoCrudService.getTodos())
    expect(6).toBe(todoBeforeUpdate.length)

    const foundCreated = todoBeforeUpdate.find(t => t.id == todoUpdated.id)!!
    expect(foundCreated.id).toEqual(todoUpdated.id)
    expect(foundCreated.title).not.toEqual(todoUpdated.title)
    expect(foundCreated.completed).not.toEqual(todoUpdated.completed)

    await firstValueFrom(todoCrudService.updateTodo(todoUpdated)).then(
      async() => {
        const todos = await firstValueFrom(todoCrudService.getTodos())
        expect(6).toBe(todos.length)

        const foundCreated = todos.find(t => t.id == todoUpdated.id)!!
        expect(foundCreated.id).toEqual(todoUpdated.id)
        expect(foundCreated.title).toEqual(todoUpdated.title)
        expect(foundCreated.completed).toEqual(todoUpdated.completed)
      }
    )
  })

  it("todoCrudService.deleteTodo()", async() => {
    expect(6).toBe((await firstValueFrom(todoCrudService.getTodos())).length)

    await firstValueFrom(todoCrudService.deleteTodo(6))
      .then(
        async() => {
          const todos = await firstValueFrom(todoCrudService.getTodos())
          expect(5).toBe(todos.length)
          expect(todos.map(t => t.id)).not.toContain(
            todoDeletedId
          )
          expect(expectedTodos).toEqual(convertTodoDateOfStringRepresentationToDateArray(todos))
        }
      )
  })
})
