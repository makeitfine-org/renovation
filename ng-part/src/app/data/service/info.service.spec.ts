/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {TestBed} from "@angular/core/testing"
import {HttpClientModule} from "@angular/common/http"
import {InfoService} from "src/app/data/service/info.service"
import {firstValueFrom} from "rxjs"
import {About} from "../../component/about-page/about-page.component"

describe("AboutPageComponent", () => {
  let infoService: InfoService

  beforeAll(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientModule ],
    })
    infoService = TestBed.inject(InfoService)
  })

  it("module", (done: DoneFn) => {
    infoService.getModule().subscribe((value) => {
      expect(value).toEqual("Hi, it's \"Renovation info\" module")
      done()
    })
  })

  it(`about / name & description`, async() => {
    const about = await firstValueFrom(infoService.getAbout())
      .then(about => {
          return about
        }
      )

    expect((about as About)?.name).toEqual("renovation info module")
    expect((about as About)?.description).toEqual("Module work as additional info directory")
  })
})
