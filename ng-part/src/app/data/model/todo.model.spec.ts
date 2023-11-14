/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Rating} from "./rating.model"
import {Todo} from "./todo.model"

describe("rating.modal.spec.ts", () => {

  it("create class", () => {

      const todo: Todo = {
        id: 1,
        title: "todo title",
        completed: true,
        description: true,
        category: "category todo",
        image: "image todo",
        price: 10,
        rating: {
          priority: 1
        } as Rating,
        date: new Date(),
      }

      expect(todo).toEqual({
        id: 1,
        title: "todo title",
        completed: true,
        description: true,
        category: "category todo",
        image: "image todo",
        price: 10,
        rating: {
          priority: 1
        } as Rating,
        date: new Date(),
      })
    }
  )
})

