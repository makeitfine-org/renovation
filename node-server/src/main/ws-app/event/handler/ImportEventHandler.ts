/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventHandler} from "@main/ws-app/event/handler/EventHandler"
import {ImportantEvent} from "@main/ws-app/event/ImportantEvent"

export class ImportEventHandler implements EventHandler<ImportantEvent> {

  handle(event: ImportantEvent): void {
    console.log("hello from important")
  }
}
