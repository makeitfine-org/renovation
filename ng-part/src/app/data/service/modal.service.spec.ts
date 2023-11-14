import {TestBed} from "@angular/core/testing"
import {ModalService} from "./modal.service"

describe("ModalService", () => {
  let service: ModalService

  beforeEach(() => {
    TestBed.configureTestingModule({})
    service = TestBed.inject(ModalService)
  })

  it("should be created", () => {
    expect(service).toBeTruthy()
  })

  it("default value", () => {
    service.isVisible$.subscribe((message: boolean) => {
      expect(message).toEqual(false)
    })
  })

  it("open method", () => {
    let callAmount = 0 // first call default method
    service.isVisible$.subscribe((message: boolean) => {
      if (callAmount++) {
        expect(message).toEqual(true)
      }
    })

    service.open()
  })

  it("open method", () => {
    let callAmount = 0 // first call default method
    service.isVisible$.subscribe((message: boolean) => {
      if (callAmount++) {
        expect(message).toEqual(false)
      }
    })

    service.close()
  })
})
