/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {IEvent} from "@main/ws-app/event/IEvent"
import {EventType} from "@main/ws-app/event/EventType"

export class Event<T = {}> implements IEvent<T> {
  readonly type: EventType
  readonly data: T

  constructor(type: EventType, data: T) {
    this.type = type
    this.data = data
  }
}
