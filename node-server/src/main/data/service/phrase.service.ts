/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {defaultPhrases, Phrase} from "@main/data/model/phrase.model"
import cloneDeep from "lodash.clonedeep"

//todo: write test
export class PhraseService {
  private static instance: PhraseService
  private phrases: Phrase[] = []

  private constructor() {
    this.phrases = defaultPhrases()
  }

  public static getInstance(): PhraseService {
    if (!PhraseService.instance) {
      PhraseService.instance = new PhraseService()
    }
    return PhraseService.instance
  }

  getPhrase = (): Phrase[] => cloneDeep(this.phrases)

  getPhraseById = (id: number): Phrase | null => cloneDeep(this.phrases.find(p => p.id == id) || null)

  addPhrase = (phrase: Phrase): void => {
    this.phrases.push(cloneDeep(phrase))
  }

  updatePhrase = (phrase: Phrase) => { //todo: add case if no element
    const index = this.phrases.findIndex((p => p.id == phrase.id))
    this.phrases[index] = cloneDeep(phrase)
  }

  removePhrase = (phrase: Phrase) => this.removePhraseById(phrase.id) //todo: add case if no element

  //todo: add case if no element
  removePhraseById = (phraseId: number) => {
    this.phrases = this.phrases.filter(p => p.id !== phraseId)
  }
}
