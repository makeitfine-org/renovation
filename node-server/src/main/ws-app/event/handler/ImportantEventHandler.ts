/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventHandler} from "@main/ws-app/event/handler/EventHandler"
import {EventType} from "@main/ws-app/event/EventType"

export class ImportantEventHandler extends EventHandler {
  readonly eventType: EventType = EventType.Important

  handleInside(data: object): object {
    return {"handler": EventType.Important}
  }
}
