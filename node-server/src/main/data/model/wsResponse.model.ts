/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

import {EventType} from "@main/ws-app/event/EventType"

export class WsResponse {
  type: EventType
  response: object | null

  constructor(type: EventType, response: object | null) {
    this.type = type
    this.response = response
  }
}
