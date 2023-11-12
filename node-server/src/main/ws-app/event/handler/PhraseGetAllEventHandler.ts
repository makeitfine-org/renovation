/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventType} from "@main/ws-app/event/EventType"
import {PhraseEventHandler} from "@main/ws-app/event/handler/PhraseEventHandler"
import {Phrase} from "@main/data/model/phrase.model"

export class PhraseGetAllEventHandler extends PhraseEventHandler {
  readonly eventType: EventType = EventType.PHRASE_GET_ALL

  handleInside(): Phrase[] | null {
    return this.phraseService.getPhrase()
  }
}
