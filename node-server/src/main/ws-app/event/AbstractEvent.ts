/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */


import {IEvent} from "@main/ws-app/event/IEvent"
import {EventType} from "@main/ws-app/event/EventType"

export abstract class AbstractEvent<T = {}> implements IEvent<T> {
  abstract readonly type: EventType
  readonly data: T

  constructor(data: T) {
    this.data = data
  }
}
