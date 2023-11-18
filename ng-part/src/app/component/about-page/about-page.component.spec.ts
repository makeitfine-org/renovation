/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {TestBed} from "@angular/core/testing"
import {AboutPageComponent} from "./about-page.component"
import {HttpClientModule} from "@angular/common/http"

describe("AboutPageComponent", () => {
  let component: AboutPageComponent

  beforeAll((done: DoneFn) => {
    TestBed.configureTestingModule({
      imports: [ HttpClientModule ],
      providers: [
        AboutPageComponent,
      ],
    })
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
})
