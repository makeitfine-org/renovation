/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {AppComponent} from "./app.component"
import {TestBed} from "@angular/core/testing"

describe("AppComponent", () => {

  // beforeEach(async() => {
  //   await TestBed.configureTestingModule({
  //     imports: [
  //       RouterTestingModule
  //     ],
  //     declarations: [
  //       AppComponent
  //     ],
  //     schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
  //   }).compileComponents()
  // })

  it(`should create the app`, () => {
    const fixture = TestBed.createComponent(AppComponent)
    const app = fixture.componentInstance
    expect(app).toBeTruthy()
  })

  it(`should have as title "ng-part"`, () => {
    const fixture = TestBed.createComponent(AppComponent)
    const app = fixture.componentInstance
    expect(app.title).toEqual("ng-part")
  })

  it("should render", () => {
    TestBed.configureTestingModule({
      declarations: [ AppComponent ]
    })

    const fixture = TestBed.createComponent(AppComponent)
    const compiled = fixture.nativeElement as HTMLElement

    expect(compiled.querySelectorAll("div.flex.h-screen.bg-gray-200.font-roboto > app-sidebar").length).toBe(1)
    expect(compiled.querySelectorAll("div.flex-1.flex.flex-col.overflow-hidden > app-header").length).toBe(1)
    expect(compiled.querySelectorAll("main.flex-1.overflow-x-hidden.overflow-y-auto.bg-gray-200 > " +
      "div.container.mx-auto.px-6.py-8 > router-outlet").length).toBe(1)
  })
})
