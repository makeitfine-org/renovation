/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventType} from "@main/ws-app/event/EventType"
import {PhraseEventHandler} from "@main/ws-app/event/handler/PhraseEventHandler"
import {Phrase} from "@main/data/model/phrase.model"
import {PhraseData} from "@main/data/model/phraseData.model"

export class PhraseGetOneEventHandler extends PhraseEventHandler {
  readonly eventType: EventType = EventType.PHRASE_GET_ONE

  handleInside(phraseData: PhraseData): Phrase | null {
    return this.phraseService.getPhraseById(phraseData.id)
  }
}
