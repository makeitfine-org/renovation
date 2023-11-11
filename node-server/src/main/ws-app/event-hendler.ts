/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import WebSocket from "ws"
import {IEvent} from "@main/ws-app/event/IEvent"
import {ImportantEventHandler} from "@main/ws-app/event/handler/ImportantEventHandler"
import {NotImportantEventHandler} from "@main/ws-app/event/handler/NotImportEventHandler"
import {OtherEventHandler} from "@main/ws-app/event/handler/OtherEventHandler"
import {Subject} from "rxjs"
import {EventType} from "@main/ws-app/event/EventType"

export const wsMessageEventOn = (messageEvent: WebSocket.RawData, ws: WebSocket) => {
  console.debug("websocket messageEvent: " + messageEvent)

  //todo: exception for json parsing / unsuitable event / errors in websocket
  // @ts-ignore
  const event = JSON.parse(messageEvent) as IEvent

  EventHandlerFacade.getInstance().handle(event)
}

class EventHandlerFacade {
  private static instance: EventHandlerFacade

  static getInstance(): EventHandlerFacade {
    if (!EventHandlerFacade.instance) {
      EventHandlerFacade.instance = new EventHandlerFacade()
    }
    return EventHandlerFacade.instance
  }

  private static map: Map<EventType, Subject<object>> = new Map()
  private static importantSubject$ = new Subject<object>()
  private static noImportantSubject$ = new Subject<object>()
  private static otherSubject$ = new Subject<object>()

  private constructor() {
    new ImportantEventHandler(EventHandlerFacade.importantSubject$)
    new NotImportantEventHandler(EventHandlerFacade.noImportantSubject$)
    new OtherEventHandler(EventHandlerFacade.otherSubject$)

    EventHandlerFacade.map.set(EventType.Important, EventHandlerFacade.importantSubject$)
    EventHandlerFacade.map.set(EventType.NotImportant, EventHandlerFacade.noImportantSubject$)
    EventHandlerFacade.map.set(EventType.Other, EventHandlerFacade.otherSubject$)
  }

  handle = (event: IEvent) => EventHandlerFacade.map.get(event.type)?.next(event.data)
}
