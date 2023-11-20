/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {TestBed} from "@angular/core/testing"
import {HttpClientModule} from "@angular/common/http"
import {InfoService} from "src/app/data/service/info.service"
import {of} from "rxjs"

describe("AboutPageComponent", () => {
  let infoService: InfoService

  beforeAll(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientModule ],
      providers: [ InfoService ]
    })
    infoService = TestBed.inject(InfoService)

    spyOn(infoService, "getAbout").and.callFake(() => {
      return of({name: "name about", description: "description about"})
    })
    spyOn(infoService, "getModule").and.callFake(() => {
      return of("some module description")
    })
  })

  it(`should return about`, (done: DoneFn) => {
    infoService.getAbout().subscribe((value) => {
      expect(value).toEqual({name: "name about", description: "description about"})
      done()
    })
  })

  it(`should return about`, (done: DoneFn) => {
    infoService.getModule().subscribe((value) => {
      expect(value).toEqual("some module description")
      done()
    })
  })
})
