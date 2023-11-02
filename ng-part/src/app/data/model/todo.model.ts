/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Rating} from "./rating.model"

export interface Todo {
  id: number
  title: string
  completed: boolean

  description?: boolean,
  category?: string,
  image?: string,
  price?: number,
  rating?: Rating,

  date?: Date
}
