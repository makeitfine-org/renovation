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

  description?: string | null,
  category?: string | null,
  image?: string | null,
  price?: number | null,
  rating?: Rating | null,

  date?: Date | null
}
