/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventType} from "@main/ws-app/event/EventType"

export abstract class EventHandler<T = object, R = object> {
  abstract readonly eventType: EventType

  handle(data: T): R {
    console.debug(`handle event>>> type: ${ this.eventType } | data: ${ JSON.stringify(data) }`)
    return this.handleInside(data)
  }

  protected abstract handleInside(data: T): R
}
