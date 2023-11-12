/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventType} from "@main/ws-app/event/EventType"
import {PhraseEventHandler} from "@main/ws-app/event/handler/PhraseEventHandler"
import {PhraseData} from "@main/data/model/phraseData.model"

export class PhraseAddOneEventHandler extends PhraseEventHandler {
  readonly eventType: EventType = EventType.PHRASE_ADD_ONE

  handleInside(phraseData: PhraseData): null {
    this.phraseService.addPhrase(phraseData.phrase!!)
    return null
  }
}
