/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventType} from "@main/ws-app/event/EventType"
import {AbstractEvent} from "@main/ws-app/event/AbstractEvent"

export class NotImportantEvent extends AbstractEvent {
  readonly type: EventType = EventType.NotImportant
}
