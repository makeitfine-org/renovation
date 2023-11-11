/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventHandler} from "@main/ws-app/event/handler/EventHandler"
import {NotImportantEvent} from "@main/ws-app/event/NotImportantEvent"

export class NotImportEventHandler implements EventHandler<NotImportantEvent> {

  handle(event: NotImportEventHandler): void {
    console.log("hello from not important")
  }
}
