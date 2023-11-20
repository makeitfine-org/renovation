import {ErrorService} from "src/app/data/service/error.service"

describe("ErrorService", () => {
  let service: ErrorService

  beforeEach(() => {
    service = new ErrorService()
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
