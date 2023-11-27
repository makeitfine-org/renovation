/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {ComponentFixture, fakeAsync, TestBed, tick, waitForAsync} from "@angular/core/testing"
import {HttpClientTestingModule} from "@angular/common/http/testing"
import {TodoComponent} from "./todo.component"
import {TodoService} from "../../data/service/todo.service"
import {of} from "rxjs"
import {Todo} from "../../data/model/todo.model"
import {FormsModule} from "@angular/forms"
import {TodoTitleFilterPipe} from "../../pipe/todo-filter.pipe"

describe("TodoComponent ts (unit)", () => {
  const TEST_TODOS: Todo[] = [
    {id: 1, title: "title 1", completed: true},
    {id: 10, title: "title 10", completed: false}
  ]

  let fixture: ComponentFixture<TodoComponent>
  let component: TodoComponent
  let todoService: jasmine.SpyObj<TodoService>

  const todoServiceSpy = jasmine.createSpyObj("TodoService",
    [ "fetchTodos", "toggleCompletedFlag", "removeTodo", "addTodo" ],
  )

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        FormsModule
      ],
      providers: [
        {provide: TodoService, useValue: todoServiceSpy},
      ],
      declarations: [
        TodoTitleFilterPipe,
        TodoComponent,
      ],
      // schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    })//.compileComponents()

    fixture = TestBed.createComponent(TodoComponent)
    component = fixture.componentInstance

    todoService = TestBed.inject(TodoService) as jasmine.SpyObj<TodoService>
  })

  it("should create the app", () => {
    expect(component).toBeTruthy()
  })

  it("should html elements and their data (empty todos)", () => {
    todoService.fetchTodos.and.returnValue(of([]))

    fixture.detectChanges()

    const compiled = fixture.nativeElement as HTMLElement
    expect(compiled.querySelector("div.container > p")?.textContent).toEqual("Loading...")

    fixture.whenStable().then(() => {
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

    const compiled = fixture.nativeElement as HTMLElement

    fixture.autoDetectChanges()
    // fixture.detectChanges()

    expect(compiled.querySelector("div.container > p")?.textContent).toEqual("Loading...")
    expect(compiled.querySelector("div.filter > input")).toBeNull()
    expect(component.loading).toBe(true)

    tick(600)

    expect(component.loading).toBe(false)
    expect(compiled.querySelector("div.container > p")).toBeNull()
    expect(compiled.querySelector("div.filter > input")?.getAttribute("placeholder"))
    expect(compiled.querySelector("div.filter > input")?.getAttribute("placeholder"))
      .toEqual("Filter todo by title...")
    expect(compiled.querySelectorAll("button.rm").length).toBe(2)
  }))

  it("should html elements and their data (todos)", waitForAsync(() => {
    todoService.fetchTodos.and.returnValue(of(TEST_TODOS))
    Object.defineProperty(todoService, "todos", {
      get: () => TEST_TODOS,
      set: () => {
      },
      configurable: true
    })

    fixture.autoDetectChanges()

    const compiled = fixture.nativeElement as HTMLElement

    fixture.whenStable().then(()=>{
      expect(component.loading).toBe(false)
      expect(compiled.querySelector("div.container > p")).toBeNull()
      expect(compiled.querySelector("div.filter > input")?.getAttribute("placeholder"))
      expect(compiled.querySelector("div.filter > input")?.getAttribute("placeholder"))
        .toEqual("Filter todo by title...")
    })
  }))
})
