/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Todo} from "../model/todo.model"

export const expectedTodos: Todo[] = [
  {
    id: 1,
    title: "Add Casandra support",
    completed: false,
    description: "Subj. Add its support as separate module with functionality or extend backend module.",
    category: "IT",
    image: "/assets/images/img1.jpg",
    price: 0,
    rating: {"priority": 5},
    date: new Date("2023-10-18T14:10:30")
  },
  {
    id: 2,
    title: "Buy sport products",
    completed: true,
    description: "Buy protein, creatine, glutamine",
    category: "Other",
    image: "/assets/images/img2.jpg",
    price: 2000,
    rating: null,
    date: new Date("2022-03-18T13:10")
  },
  {
    id: 3,
    title: "Improve home staff",
    completed: false,
    description: "Repair some things",
    category: "Other",
    image: null,
    price: 500.5,
    rating: {"priority": 3},
    date: new Date("2023-06-11T10:10")
  },
  {
    id: 4,
    title: "Add kubernates yamls",
    completed: false,
    description: "Improve kubernetes yaml and helm configs in project",
    category: "IT",
    image: "/assets/images/img4.jpg",
    price: null,
    rating: {"priority": 4},
    date: new Date("2023-08-05T13:10")
  },
  {
    id: 5,
    title: "Add Postgress configs",
    completed: true,
    description: null,
    category: "IT",
    image: "/assets/images/img5.jpg",
    price: null,
    rating: {priority: 0},
    date: new Date("2022-03-10T20:15")
  }
]

export const convertTodoDateOfStringRepresentationToDateArray = (todos: Todo[]) =>
  todos.map(todo => convertTodoDateOfStringRepresentationToDate(todo))

const convertTodoDateOfStringRepresentationToDate = (todo: Todo) => {
  return {
    ...todo,
    date: (todo.date !== null && todo.date !== undefined ? new Date(todo.date.toString()) : null)
  }
}
