/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {IEvent} from "@main/ws-app/event/IEvent"

export interface EventHandler<T = IEvent> {

  handle(event: T): void
}
