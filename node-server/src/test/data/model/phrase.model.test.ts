/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import {defaultPhrases, Phrase} from "@main/data/model/phrase.model"

describe("phrase.model.ts", () => {
    test("Phrase interface: create object", () => {
        const phrase: Phrase = testPhrase() as Phrase

        expect(phrase).toEqual(testPhrase())

        expect(phrase).not.toBe(undefined)
        expect(phrase).not.toBe(null)
        expect(phrase).not.toEqual({
            id: 100,
            title: "test title 0",
            text: "test text 0",
        })
    })

    test("Phrase interface: create object", () => {
        class PhraseClass implements Phrase {
            id = 10
            title = "test title"
            text = "test text"
        }

        const phraseClass = new PhraseClass()

        expect(phraseClass).toEqual(testPhrase())
        expect(phraseClass.id).toBe(testPhrase().id)
        expect(phraseClass.title).toBe(testPhrase().title)
        expect(phraseClass.text).toBe(testPhrase().text)

        expect(phraseClass).not.toBe(undefined)
        expect(phraseClass).not.toBe(null)
        expect(phraseClass).not.toEqual({
            id: 100,
            title: "test title 0",
            text: "test text 0",
        })
    })

    test("defaultPhrases", () => {

        const defaultPhrases1 = defaultPhrases()
        const defaultPhrases2 = defaultPhrases()

        expect(defaultPhrases1.length).toBe(5)
        expect(defaultPhrases1).toEqual([
            {id: 1, title: "hi", text: "Hello everyone!"},
            {id: 2, title: "place", text: "I'm here!"},
            {id: 3, title: "question", text: "Who there?"},
            {id: 4, title: "bed words", text: "Damn!"},
            {id: 5, title: "quite", text: "I'm off"}
        ])

        expect(defaultPhrases1).not.toBe(defaultPhrases2)
        expect(defaultPhrases1).toEqual(defaultPhrases2)
    })

    function testPhrase() {
        return {
            id: 10,
            title: "test title",
            text: "test text",
        }
    }
})
