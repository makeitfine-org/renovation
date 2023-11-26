/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {ComponentFixture, TestBed} from "@angular/core/testing"
import {FormsComponent} from "./forms.component"

describe("FormsComponent ts (unit)", () => {
  let fixture: ComponentFixture<FormsComponent>
  let component: FormsComponent

  beforeEach(() => {
    TestBed.configureTestingModule({
      // imports: [ ReactiveFormsModule ],
      providers: [],
      declarations: [
        FormsComponent
      ],
    })
  })

  beforeEach(() => {
    fixture = TestBed.createComponent(FormsComponent)
    component = fixture.componentInstance
  })

  it("should create the app", () => {
    expect(component).toBeTruthy()
  })
})
