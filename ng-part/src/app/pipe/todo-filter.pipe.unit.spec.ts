/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Todo} from "src/app/data/model/todo.model"
import {TodoTitleFilterPipe} from "./todo-filter.pipe"

describe("TodoTitleFilterPipe", () => {
  // This pipe is a pure, stateless function so no need for BeforeEach
  const pipe = new TodoTitleFilterPipe()
  let todos: Todo[]

  beforeEach(() => {
    todos = [
      {id: 1, title: "I was born", completed: true},
      {id: 2, title: "born Was", completed: true},
      {id: 3, title: "quest it one", completed: false},
      {id: 4, title: "born wAs", completed: true},
      {id: 1, title: "Quest it one", completed: true},
      {id: 1, title: "Quest it one again", completed: true},
      {id: 1, title: "born was", completed: true}
    ]
  })

  it("find 'born was'", () => {
    expect(pipe.transform(todos, "born was")).toEqual([
      {id: 2, title: "born Was", completed: true},
      {id: 4, title: "born wAs", completed: true},
      {id: 1, title: "born was", completed: true}
    ])
  })

  it("find 'born Was'", () => {
    expect(pipe.transform(todos, "born Was")).toEqual([
      {id: 2, title: "born Was", completed: true},
      {id: 4, title: "born wAs", completed: true},
      {id: 1, title: "born was", completed: true}
    ])
  })

  it("find 'not exist'", () => {
    expect(pipe.transform(todos, "not exist")).toEqual([])
  })

  it("find 'quest it one again'", () => {
    expect(pipe.transform(todos, "quest it one again")).toEqual([ {id: 1, title: "Quest it one again", completed: true} ])
  })

  it("find with empty string", () => {
    expect(pipe.transform(todos, "")).toEqual(todos)
  })

  it("find with spaces in search string", () => {
    expect(pipe.transform(todos, "  ")).toEqual(todos)
  })

  it("find with spaces in search string", () => {
    expect(pipe.transform(todos)).toEqual(todos)
  })
})
