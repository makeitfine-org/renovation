/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {EventHandler} from "@main/ws-app/event/handler/EventHandler"

export class NotImportantEventHandler extends EventHandler {

  handle(data: object): void {
    console.debug(`${ data }`)
  }
}

