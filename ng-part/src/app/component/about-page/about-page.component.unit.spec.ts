/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {ComponentFixture, TestBed} from "@angular/core/testing"
import {AboutPageComponent} from "./about-page.component"
import {HttpClientTestingModule} from "@angular/common/http/testing"
import {InfoService} from "../../data/service/info.service"
import {of} from "rxjs"

describe("AboutPageComponent ts (unit)", () => {
  let aboutPageComponentFixture: ComponentFixture<AboutPageComponent>

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
// TitleCasePipe
      ],
      declarations: [
        AboutPageComponent,
// MockPipe
      ]
    })
    aboutPageComponentFixture = TestBed.createComponent(AboutPageComponent)
  })

  it("should create the app", () => {
    const app = aboutPageComponentFixture.componentInstance
    expect(app).toBeTruthy()
  })

  it("should html elements and their info", () => {
    aboutPageComponentFixture.detectChanges()
    const compiled = aboutPageComponentFixture.nativeElement as HTMLElement
    expect(compiled.querySelector("h1.font-bold.mb-2.text-center.text-lg")?.textContent).toContain("Extra Information")
    expect(compiled.querySelector("div > p.mb-2 > span")?.textContent)
      .toContain("Details of module used for backend requests:")

    expect(Array.from(compiled.querySelectorAll("div > p.ml-2 > span")).map(e => e.textContent))
      .toEqual([
        " about ",
        " description ",
        " module info: "
      ])
  })

  it(`should return about and module info`, () => {
    const aboutPageComponent = aboutPageComponentFixture.componentInstance
    let service = aboutPageComponentFixture.debugElement.injector.get(InfoService)

    spyOn(service, "getAbout").and.callFake(() => {
      return of({name: "name about", description: "description about"})
    })
    spyOn(service, "getModule").and.callFake(() => {
      return of("some module description")
    })

    aboutPageComponentFixture.detectChanges() // same effect when calling: aboutPageComponent.ngOnInit()
    expect(aboutPageComponent.about).toEqual({name: "name about", description: "description about"})
    expect(aboutPageComponent.about).not.toEqual({name: "(other) name about", description: "(other) description about"})

    expect(aboutPageComponent.module).toEqual("some module description")
    expect(aboutPageComponent.module).not.toEqual("(other) some module description")
  })
})
