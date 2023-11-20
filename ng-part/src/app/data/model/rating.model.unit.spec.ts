/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Rating} from "./rating.model"

describe("rating.modal.spec.ts", () => {

  it("create class", () => {

    const rating: Rating = {priority: 1000}

    expect(rating).toEqual({priority: 1000})
  })
})

