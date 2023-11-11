/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import WebSocket from "ws"
import {IEvent} from "@main/ws-app/event/IEvent"
import {EventType} from "@main/ws-app/event/EventType"
import {ImportEventHandler} from "@main/ws-app/event/handler/ImportEventHandler"
import {NotImportEventHandler} from "@main/ws-app/event/handler/NotImportEventHandler"
import {OtherEvent} from "@main/ws-app/event/OtherEvent"
import {ImportantEvent} from "@main/ws-app/event/ImportantEvent"
import {NotImportantEvent} from "@main/ws-app/event/NotImportantEvent"
import {EventHandler} from "@main/ws-app/event/handler/EventHandler"

export const wsMessageEventOn = (messageEvent: WebSocket.RawData) => {
  console.log("websocket messageEvent: " + messageEvent)

  //todo: exception for json parsing / unsuitable event / errors in websocket
  const event = JSON.parse(messageEvent) as IEvent

  EventHandlerFacade.getInstance().handle(event)
}

class EventHandlerFacade {
  private static instance: EventHandlerFacade

  private static events: typeof = [ ImportantEvent, NotImportantEvent, OtherEvent ]
  private static handlers: EventHandler[] = [ new ImportEventHandler(), new NotImportEventHandler() ]

  static getInstance(): EventHandlerFacade {
    if (!EventHandlerFacade.instance) {
      EventHandlerFacade.instance = new EventHandlerFacade()
    }
    return EventHandlerFacade.instance
  }

  handle(event: IEvent) {
    Object
      .values(EventType)
      .filter((value) => {
        console.log(value)
      })

  }

  private defineEvent(event: IEvent) {
    EventHandlerFacade.events.find(event => event typeof event)
  }
}
