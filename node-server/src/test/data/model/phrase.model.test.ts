/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import {Phrase} from "@main/data/model/phrase.model"

describe("phrase.model.ts: Phrase interface", () => {
  test("create object", () => {
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

  test("create object", () => {
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

  function testPhrase() {
    return {
      id: 10,
      title: "test title",
      text: "test text",
    }
  }
})
