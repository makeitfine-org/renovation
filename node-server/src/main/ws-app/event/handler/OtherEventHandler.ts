/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventHandler} from "@main/ws-app/event/handler/EventHandler"
import {EventType} from "@main/ws-app/event/EventType"

export class OtherEventHandler extends EventHandler {
  readonly eventType: EventType = EventType.Other

  handle(data: object): object {
    console.debug(`${ data }`)
    return {"handler": EventType.Other}
  }
}

