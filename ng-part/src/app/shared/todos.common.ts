/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

export class Constants {
  static readonly TIME_DATE_FORMAT: string = "yyyy-MM-dd HH:mm"
}

export interface Todo {
  id: number
  title: string
  completed: boolean
  date?: Date
}
