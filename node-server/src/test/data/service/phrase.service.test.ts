/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

// @ts-ignore
import {PhraseService} from "@main/data/service/phrase.service"
// @ts-ignore
import {Phrase} from "@main/data/model/phrase.model"

//order of tests should be kept
describe("phrase.service.ts", () => {
  let phraseService: PhraseService

  beforeAll(() => phraseService = PhraseService.getInstance())

  test("singleton", () => {
    const phraseServiceInstance2 = PhraseService.getInstance()

    expect(phraseService).toBe(phraseServiceInstance2)
  })

  test("getPhrase", () => {
    expect(phraseService.getPhrase()).toEqual([
      {id: 1, title: "hi", text: "Hello everyone!"},
      {id: 2, title: "place", text: "I'm here!"},
      {id: 3, title: "question", text: "Who there?"},
      {id: 4, title: "bed words", text: "Damn!"},
      {id: 5, title: "quite", text: "I'm off"}
    ])
    expect(phraseService.getPhrase()).not.toEqual([
      {id: 1, title: "hi", text: "Hello everyone!"},
      {id: 2, title: "place", text: "I'm here!"},
      {id: 4, title: "bed words", text: "Damn!"},
      {id: 3, title: "question", text: "Who there?"},
      {id: 5, title: "quite", text: "I'm off"}
    ])
    expect(phraseService.getPhrase()).not.toEqual([
      {id: 1, title: "hi", text: "Hello everyone!"},
      {id: 2, title: "place", text: "I'm here!"},
      {id: 3, title: "question", text: "Who there?"},
      {id: 4, title: "bed words", text: "Damn!"}
    ])
  })

  test("getPhrase (check if it's returned list deepCopy by adding new element)", () => {
    const phraseList = phraseService.getPhrase()
    const addedPhrase: Phrase = {id: 1_000_000, title: "any title", text: "any text"}
    phraseList.push(addedPhrase)

    expect(phraseService.getPhrase().length).toBeLessThan(phraseList.length)
    expect(phraseService.getPhrase().length).toBe(phraseList.length - 1)
    expect(phraseService.getPhrase()).not.toContainEqual(addedPhrase)

    expect(phraseList).toEqual(expect.arrayContaining(phraseService.getPhrase()))
  })

  test("getPhrase (check if it's returned list deepCopy by removing element)", () => {
    const phraseList = phraseService.getPhrase()
    phraseList.shift()
    phraseList.pop()

    expect(phraseService.getPhrase().length).toBeGreaterThan(phraseList.length)
    expect(phraseService.getPhrase().length).toBe(phraseList.length + 2)
    expect(phraseService.getPhrase()).toEqual(expect.arrayContaining(phraseList))
  })

  test("getPhrase (check if it's returned list deepCopy by removing element)", () => {
    const phraseList = phraseService.getPhrase()
    phraseList.shift()
    phraseList.pop()

    expect(phraseService.getPhrase().length).toBeGreaterThan(phraseList.length)
    expect(phraseService.getPhrase().length).toBe(phraseList.length + 2)
    expect(phraseService.getPhrase()).toEqual(expect.arrayContaining(phraseList))
  })

  test("getPhraseById", () => {
    expect(phraseService.getPhraseById(3)).toEqual({id: 3, title: "question", text: "Who there?"})
  })

  test("getPhraseById (not existence)", () => {
    expect(phraseService.getPhraseById(1000_000)).toBeNull()
  })

  test("getPhraseById (check if it's returned deepCopy of the element)", () => {
    const id = 3
    let phrase = phraseService.getPhraseById(id)

    expect(phraseService.getPhraseById(id)).toEqual(phrase)

    phrase = phrase ?? {id: 0, title: "", text: ""}
    phrase.id = phrase.id + 1_000_000
    phrase.title = phrase.title + " 0"
    phrase.text = phrase.text + " 0"

    expect(phraseService.getPhraseById(id)).not.toEqual(phrase)
  })

  test("getPhraseById", () => {
    expect(phraseService.getPhraseById()).toEqual(null)
  })

  test("addPhrase and check it's added as deep copy", () => {
    const id = 1_000_000
    const addedPhrase: Phrase = {id, title: "any title", text: "any text"}
    const phraseLengthBeforeAdding = phraseService.getPhrase().length

    phraseService.addPhrase(addedPhrase)
    expect(phraseService.getPhraseById(id)).toEqual(addedPhrase)
    expect(phraseService.getPhraseById(id) === addedPhrase).toBeFalsy()
    expect(phraseService.getPhrase().length).toBe(phraseLengthBeforeAdding + 1)
    expect(phraseService.getPhrase()).toContainEqual(addedPhrase)

    addedPhrase.title = "changed"
    expect(phraseService.getPhraseById(id)).not.toEqual(addedPhrase)
    expect(phraseService.getPhrase()).not.toContainEqual(addedPhrase)
  })

  test("updatePhrase and check it's updated as deep copy", () => {
    const id = 5
    const updatedPhrase: Phrase = phraseService.getPhraseById(id) ?? {id: 0, title: "", text: ""}

    updatedPhrase.title = "title updated"
    updatedPhrase.text = "text updated"
    expect(phraseService.getPhraseById(id)).not.toEqual(updatedPhrase)
    phraseService.updatePhrase(updatedPhrase)
    expect(phraseService.getPhraseById(id)).toEqual(updatedPhrase)

    updatedPhrase.title = "title updated again"
    updatedPhrase.text = "text updated again"
    expect(phraseService.getPhraseById(id)).not.toEqual(updatedPhrase)
  })

  test("removePhrase", () => {
    const id = 3
    const phrase = phraseService.getPhraseById(id)

    expect(phraseService.getPhrase()).toContainEqual(phrase)

    phraseService.removePhrase(phrase!)

    expect(phraseService.getPhrase()).not.toContainEqual(phrase)
  })

  test("removePhraseById", () => {
    const id = 5
    const phrase = phraseService.getPhraseById(id)

    expect(phraseService.getPhrase()).toContainEqual(phrase)

    phraseService.removePhraseById(id)

    expect(phraseService.getPhrase()).not.toContainEqual(phrase)
    expect(phraseService.removePhraseById(-1)).toBe(undefined)
  })
})
