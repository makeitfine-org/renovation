/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventType} from "@main/ws-app/event/EventType"

export abstract class EventHandler<T = object, R = object> {
  abstract readonly eventType: EventType

  abstract handle(data: T): R
}
