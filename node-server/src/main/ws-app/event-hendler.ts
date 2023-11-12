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
import {EventHandler} from "@main/ws-app/event/handler/EventHandler"
import {PhraseGetAllEventHandler} from "@main/ws-app/event/handler/PhraseGetAllEventHandler"
import {WsResponse} from "@main/data/model/wsResponse.model"


export const wsMessageEventOn = (messageEvent: WebSocket.RawData, ws: WebSocket) => {
  console.debug("websocket messageEvent: " + messageEvent)

  //todo: exception for json parsing / unsuitable event / errors in websocket
  // @ts-ignore
  const event = JSON.parse(messageEvent.toString()) as IEvent

  EventHandlerFacade.getInstance().handle(event).forEach(r => ws.send(
    JSON.stringify(new WsResponse(event.type, r))
  ))
}

class EventHandlerFacade {
  private static instance: EventHandlerFacade

  static getInstance(): EventHandlerFacade {
    if (!EventHandlerFacade.instance) {
      EventHandlerFacade.instance = new EventHandlerFacade()
    }
    return EventHandlerFacade.instance
  }

  private static handlers: EventHandler[] = []

  private constructor() {
    EventHandlerFacade.handlers.push(new ImportantEventHandler())
    EventHandlerFacade.handlers.push(new NotImportantEventHandler())
    EventHandlerFacade.handlers.push(new OtherEventHandler())

    EventHandlerFacade.handlers.push(new PhraseGetAllEventHandler())
  }

  handle = (event: IEvent): (object | null)[] => EventHandlerFacade.handlers //todo: test for null
    .filter(h => h.eventType === event.type)
    .map(h => h.handle(event.data))
}
