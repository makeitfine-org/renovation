/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {ComponentFixture, fakeAsync, TestBed, tick} from "@angular/core/testing"
import {HttpClientTestingModule} from "@angular/common/http/testing"
import {TodoComponent} from "./todo.component"
import {TodoService} from "../../data/service/todo.service"
import {of} from "rxjs"
import {Todo} from "../../data/model/todo.model"
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core"
import {FormsModule} from "@angular/forms"
import {TodoTitleFilterPipe} from "../../pipe/todo-filter.pipe"

describe("TodoComponent ts (unit)", () => {
  const TEST_TODOS: Todo[] = [
    {id: 1, title: "title 1", completed: true},
    {id: 10, title: "title 10", completed: false}
  ]

  let todoComponentFixture: ComponentFixture<TodoComponent>
  let todoService: jasmine.SpyObj<TodoService>

  const todoServiceSpy = jasmine.createSpyObj("TodoService",
    [ "fetchTodos", "toggleCompletedFlag", "removeTodo", "addTodo" ],
  )

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        FormsModule
      ],
      providers: [
        // TodoTitleFilterPipe,
        {provide: TodoService, useValue: todoServiceSpy},
      ],
      declarations: [
        TodoTitleFilterPipe,
        TodoComponent,
        // MockPipe
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    }).compileComponents()

    todoComponentFixture = TestBed.createComponent(TodoComponent)
    todoService = TestBed.inject(TodoService) as jasmine.SpyObj<TodoService>
  })

  it("should create the app", () => {
    const app = todoComponentFixture.componentInstance
    expect(app).toBeTruthy()
  })

  it("should html elements and their data (empty todos)", async() => {
    todoService.fetchTodos.and.returnValue(of([]))

    todoComponentFixture.detectChanges()

    const compiled = todoComponentFixture.nativeElement as HTMLElement
    expect(compiled.querySelector("div.container > p")?.textContent).toEqual("Loading...")

    todoComponentFixture.whenStable().then(() => {
      expect(compiled.querySelector("div.container > p")?.textContent).toEqual("Loading...")
    })
  })

  it("should html elements and their data (todos)", fakeAsync(() => {
    todoService.fetchTodos.and.returnValue(of(TEST_TODOS))
    Object.defineProperty(todoService, "todos", {
      get: () => TEST_TODOS,
      set: () => {
      },
      configurable: true
    })

    const todoComponent = todoComponentFixture.componentInstance
    const compiled = todoComponentFixture.nativeElement as HTMLElement

    todoComponentFixture.autoDetectChanges()
    todoComponentFixture.detectChanges()

    expect(compiled.querySelector("div.container > p")?.textContent).toEqual("Loading...")
    expect(compiled.querySelector("div.filter > input")).toBeNull()
    expect(todoComponent.loading).toBe(true)

    tick(600)

    expect(todoComponent.loading).toBe(false)
    expect(compiled.querySelector("div.container > p")).toBeNull()
    expect(compiled.querySelector("div.filter > input")?.getAttribute("placeholder"))
      .toEqual("Filter todo by title...")
  }))
})
