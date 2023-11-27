/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {AppComponent} from "./app.component"
import {ComponentFixture, fakeAsync, TestBed, tick, waitForAsync} from "@angular/core/testing"

describe("AppComponent", () => {
  let fixture: ComponentFixture<AppComponent>
  let component: AppComponent

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ AppComponent ]
    })

    fixture = TestBed.createComponent(AppComponent)
    component = fixture.componentInstance
  })

  it(`should create the app`, () => {
    expect(component).toBeTruthy()
  })

  it(`should have as title "ng-part"`, () => {
    jasmine.clock().install()
    component.setTitle()
    jasmine.clock().tick(200)
    expect(component.title).toEqual("ng-part")
    jasmine.clock().uninstall()
  })

  it(`should have as title "ng-part" (fakeAsync)`, fakeAsync(() => {
    component.setTitle().then(() => {
      expect(component.title).toEqual("ng-part")
    })
    tick(200)
  }))

  it(`should have as title "ng-part" (waitForAsync)`, waitForAsync(() => {
    component.setTitle().then(() => {
      expect(component.title).toEqual("ng-part")
    })
  }))

  it(`should have not have as title "ng-part"`, () => {
    jasmine.clock().install()
    component.setTitle()
    jasmine.clock().tick(50)
    expect(component.title).not.toEqual("ng-part")
    jasmine.clock().uninstall()
  })

  it("should render", () => {
    const compiled = fixture.nativeElement as HTMLElement

    expect(compiled.querySelectorAll("div.flex.h-screen.bg-gray-200.font-roboto > app-sidebar").length).toBe(1)
    expect(compiled.querySelectorAll("div.flex-1.flex.flex-col.overflow-hidden > app-header").length).toBe(1)
    expect(compiled.querySelectorAll("main.flex-1.overflow-x-hidden.overflow-y-auto.bg-gray-200 > " +
      "div.container.mx-auto.px-6.py-8 > router-outlet").length).toBe(1)
  })
})
