/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Phrase} from "../model/phrase.model"

//todo: write test
export class PhraseService {
  public phrases: Phrase[] = []

  constructor() {
    this.phrases = [ {
      id: 1, title: "hi", text: "Hello everyone!!!"
    }, {
      id: 2, title: "place", text: "I'm here!!!"
    }, {
      id: 3, title: "question", text: "Who there???"
    }, {
      id: 4, title: "bed words", text: "Damn!"
    }, {
      id: 5, title: "quite", text: "I'm off"
    } ]
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
