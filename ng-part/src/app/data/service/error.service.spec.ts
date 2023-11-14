import {TestBed} from "@angular/core/testing"

import {ErrorService} from "src/app/data/service/error.service"

describe("ErrorService", () => {
  let service: ErrorService

  beforeEach(() => {
    TestBed.configureTestingModule({})
    service = TestBed.inject(ErrorService)
  })

  it("should be created", () => {
    expect(service).toBeTruthy()
  })

  it("handle method", () => {
    service.error$.subscribe((message: string) => {
      expect(message).toEqual("some error message")
    })

    service.handle("some error message")
  })

  it("clear method", () => {
    service.error$.subscribe((message: string) => {
      expect(message).toEqual("")
    })

    service.clear()
  })
})
