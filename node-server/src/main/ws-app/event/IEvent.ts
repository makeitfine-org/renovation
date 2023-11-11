/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventType} from "@main/ws-app/event/EventType"

export interface IEvent<T = {}> {
  readonly type: EventType
  readonly data: T
}
