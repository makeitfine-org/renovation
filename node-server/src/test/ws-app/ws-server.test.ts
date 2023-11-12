/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import * as http from "http"
import {WsServerFactory} from "../../main/ws-app/ws-server-factory"
import WebSocket from "ws"
import {Constant} from "../../main/Constant"
import {PhraseService} from "../../main/data/service/phrase.service"
import {WsResponse} from "../../main/data/model/wsResponse.model"

describe("WebSocket Server", () => {
  const DEFAULT_TEST_PORT = 3111
  const port = DEFAULT_TEST_PORT
  const phrasesService = PhraseService.getInstance()

  let server: http.Server
  let client: WebSocket

  let responseMessage: object | null = null

  beforeAll(async() => {
    // Start server
    server = await startServer(port)

    // Create test client
    client = new WebSocket(`ws://localhost:${ port }`)
    await waitForSocketState(client, client.OPEN)

    // @ts-ignore
    client.on("message", (data) => {
      responseMessage = JSON.parse(data.toString())
    })
  })

  afterAll(async() => {
    // Close the client after it receives the response
    client.close()

    await waitForSocketState(client, client.CLOSED)
    // Close server
    server.close()
  })

  test("Send 'important type' message and get response", async() => {
    const testMessage: string = `
    {
      "type" : "important",
      "data" : {
                  "message":"hello"
               }
    }
    `
    await sendMessage(testMessage)

    // Perform assertions on the response
    expect(
      responseMessage)
      .toEqual({
        "response": {
          "data": {
            "message": "hello"
          },
          "handler": "important"
        },
        "type": "important"
      })
  })

  test("Send 'not important' type message and get response", async() => {
    const testMessage: string = `
    {
      "type" : "not_important",
      "data" : {
                  "message":"hello 2"
               }
    }
    `
    await sendMessage(testMessage)

    // Perform assertions on the response
    expect(
      responseMessage)
      .toEqual({
        "response": {
          "data": {
            "message": "hello 2"
          },
          "handler": "not_important"
        },
        "type": "not_important"
      })
  })

  test("Send 'other' type message and get response", async() => {
    const testMessage: string = `
    {
      "type" : "other",
      "data" : {
                  "message":"hello"
               }
    }
    `
    await sendMessage(testMessage)

    // Perform assertions on the response
    expect(
      responseMessage)
      .toEqual({
        "response": {
          "data": {
            "message": "hello"
          },
          "handler": "other"
        },
        "type": "other"
      })
  })

  test("Send 'phrase_get_all' type message and get response", async() => {
    const testMessage: string = `
    {
      "type" : "phrase_get_all",
      "data" : null
    }
    `
    await sendMessage(testMessage)

    // Perform assertions on the response
    expect(responseMessage).not.toEqual({})

    expect(responseMessage)
      .toEqual({
        "type": "phrase_get_all",
        "response": [
          {
            "id": 1,
            "title": "hi",
            "text": "Hello everyone!"
          },
          {
            "id": 2,
            "title": "place",
            "text": "I'm here!"
          },
          {
            "id": 3,
            "title": "question",
            "text": "Who there?"
          },
          {
            "id": 4,
            "title": "bed words",
            "text": "Damn!"
          },
          {
            "id": 5,
            "title": "quite",
            "text": "I'm off"
          }
        ]
      })

    expect((responseMessage as WsResponse).response).toEqual(phrasesService.getPhrase())
  })

  const sendMessage = async(message: string, sleepMS: number = Constant.DEFAULT_SLEEP_AFTER_WS_SEND_MESSAGE) => {
    client.send(message)
    await sleep(sleepMS)
  }
})

function startServer(port: number) {
  const server = http.createServer()
  WsServerFactory.startServer(server)

  return new Promise<http.Server>((resolve) => {
    server.listen(port, () => resolve(server))
  })
}

function waitForSocketState(socket: WebSocket, state: number) {
  return new Promise(function (resolve) {
    setTimeout(function () {
      if (socket.readyState === state) {
        resolve("resolved")
      } else {
        waitForSocketState(socket, state).then(resolve)
      }
    }, 10)
  })
}

function sleep(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms))
}
