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
  })

  it("should return an error when the server returns a 404", (done: DoneFn) => {
    const errorResponse = new HttpErrorResponse({
      error: "test 404 error",
      status: 404,
      statusText: "Not Found",
    })

    httpClientSpy.get.and.returnValue(asyncError(errorResponse))

    todoCrudService.getTodos().subscribe({
      next: (heroes) => done.fail("expected an error, not todos"),
      error: (error) => {
        expect(error.message).toContain("Http failure response for (unknown url): 404 Not Found")
        done()
      },
    })
  })

  function asyncData<T>(data: T) {
    return defer(() => Promise.resolve(data))
  }

  function asyncError<T>(errorObject: any) {
    return defer(() => Promise.reject(errorObject))
  }
})

describe("TodoCrudService (real request to server)", () => {
  let httpClient: HttpClient
  let datePipe: DatePipe
  let todoCrudService: TodoCrudService

  beforeEach(() => {
    TestBed.configureTestingModule({
      // Import the HttpClient mocking services
      imports: [ HttpClientModule, ],
      // Provide the service-under-test
      providers: [ DatePipe, HttpClient ],
    })

    // Inject the http, test controller, and service-under-test
    // as they will be referenced by each test.
    httpClient = TestBed.inject(HttpClient)
    datePipe = TestBed.inject(DatePipe)
    todoCrudService = new TodoCrudService(httpClient, datePipe)
  })

  afterEach(() => {
  })

  it("get todos: http://localhost:9190/api/v1/info/todo", async() => {
    const todos = await firstValueFrom(todoCrudService.getTodos())
      .then(todos => {
          return todos
        }
      )

    expect(todos!!.length).toBe(5)
  })
})
