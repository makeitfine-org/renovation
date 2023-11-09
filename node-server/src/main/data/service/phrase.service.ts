/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {defaultPhrases, Phrase} from "@main/data/model/phrase.model"

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

  getPhrase = () => this.phrases

  addPhrase = (phrase: Phrase) => this.phrases.push(phrase)

  updatePhrase = (phrase: Phrase) => {
    const index = this.phrases.findIndex((p => p.id == phrase.id))
    this.phrases[index] = phrase
  }

  removePhrase = (phrase: Phrase) => this.removePhraseById(phrase.id)

  removePhraseById = (phraseId: number) => this.phrases = this.phrases.filter(p => p.id !== phraseId)
}
