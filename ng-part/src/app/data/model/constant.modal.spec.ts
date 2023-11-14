/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Constant} from "./constant.modal"

describe("constant.modal.spec.ts", () => {

  it("all", () => {
    expect(Constant.TIME_DATE_FORMAT).toBe("yyyy-MM-dd HH:mm")
    expect(Constant.DEFAULT_TODO_IMG).toBe("/assets/images/img0.jpg")
    expect(Constant.WS_INTENTIONAL_CLOSE_CODE).toBe(1000)
    expect(Constant.WS_INTENTIONAL_CLOSE_MESSAGE).toEqual("closed intentionally")
  })
})
