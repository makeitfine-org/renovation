/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {TestBed} from "@angular/core/testing"
import {About, AboutPageComponent} from "./about-page.component"
import {HttpClientModule} from "@angular/common/http"
import {InfoService} from "../../data/service/info.service"
import {firstValueFrom} from "rxjs"

describe("AboutPageComponent", () => {
  let component: AboutPageComponent
  let infoService: InfoService

  beforeAll((done: DoneFn) => {
    TestBed.configureTestingModule({
      imports: [ HttpClientModule ],
      providers: [
        AboutPageComponent,
      ],
    })
    infoService = TestBed.inject(InfoService)
    component = TestBed.inject(AboutPageComponent)

    component.ngOnInit()
    setTimeout(() => {
      done()
    }, 100)
  })

  it("component init", () => {
    expect(component.about).toEqual({
      name: "renovation info module",
      description: "Module work as additional info directory",
    })

    expect(component.module).toEqual("Hi, it's \"Renovation info\" module")
  })

  it("about / module", (done: DoneFn) => {
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
