/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventHandler} from "@main/ws-app/event/handler/EventHandler"
import {PhraseService} from "@main/data/service/phrase.service"
import {Phrase} from "@main/data/model/phrase.model"
import {PhraseData} from "@main/data/model/phraseData.model"

export abstract class PhraseEventHandler extends EventHandler {

  protected phraseService = PhraseService.getInstance()

  override handle(data: PhraseData): Phrase | Phrase[] | null {
    console.debug(`phrase handler > type: ${ this.eventType } | data: ${ JSON.stringify(data) }`)

    return this.handleInside(data)
  }

  abstract override handleInside(data: PhraseData | null): Phrase | Phrase[] | null
}
