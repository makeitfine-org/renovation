/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {ComponentFixture, TestBed} from "@angular/core/testing"
import {TodoDescriptionComponent} from "./todo-description.component"

describe("TodoDescriptionComponent ts (unit)", () => {
    let fixture: ComponentFixture<TodoDescriptionComponent>
    let component: TodoDescriptionComponent
    let compiled: HTMLElement

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [],
            declarations: [
                TodoDescriptionComponent
            ],
            // schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
        })//.compileComponents()
    })

    beforeEach(() => {
        fixture = TestBed.createComponent(TodoDescriptionComponent)
        component = fixture.componentInstance
        compiled = fixture.nativeElement
    })

    it("should create the app", () => {
        expect(component).toBeTruthy()
    })

    it("details", () => {
        expect(component.details).toBeFalse()
    })

    it("should html elements and their data", () => {
        component.todo = {
            id: 231,
            title: "some title",
            image: "site.some/image.jpg",
            completed: false
        }

        fixture.detectChanges()

        expect(compiled.querySelector("button.border.py-2.px-4.rounded")?.textContent)
            .toEqual(" Show Details ")

        expect(compiled.querySelector("img.mb-2")?.getAttribute("src"))
            .toEqual("site.some/image.jpg")
        expect(compiled.querySelector("img.mb-2")?.getAttribute("alt"))
            .toEqual("some title")
    })

    it("should click on button", () => {
        const button = fixture.nativeElement.querySelector("button.border.py-2.px-4.rounded")
        expect(button).not.toBeNull()

        expect(component.details).toBeFalse()
        button.click()
        expect(component.details).toBeTruthy()
    })
})
