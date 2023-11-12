/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import * as http from "http"
import {WsServerFactory} from "../../main/ws-app/ws-server-factory"
import {WebSocket} from "ws"
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

  const sendMessage = async(message: string, sleepMS: number = Constant.DEFAULT_SLEEP_AFTER_WS_SEND_MESSAGE) => {
    client.send(message)
    await sleep(sleepMS)
  }

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

  test("Send 'phrase_get_one' type message and get response", async() => {
    const id = 3
    const testMessage: string = `
    {
      "type" : "phrase_get_one",
      "data" : { "id": ${ id } }
    }
    `
    await sendMessage(testMessage)

    // Perform assertions on the response
    expect(responseMessage).not.toEqual({})

    expect(responseMessage)
      .toEqual({
        "type": "phrase_get_one",
        "response": {
          "id": id,
          "title": "question",
          "text": "Who there?"
        }
      })

    expect((responseMessage as WsResponse).response).toEqual(phrasesService.getPhraseById(id))
  })

  test("Send 'phrase_get_one' type message with not exists id and get response", async() => {
    const id = 1_001
    const testMessage: string = `
    {
      "type" : "phrase_get_one",
      "data" : { "id": ${ id } }
    }
    `
    await sendMessage(testMessage)

    // Perform assertions on the response
    expect(responseMessage)
      .toEqual({
        "type": "phrase_get_one",
        "response": null
      })

    expect((responseMessage as WsResponse).response).toEqual(phrasesService.getPhraseById(id))
  })

  test("Send 'phrase_get_one' type message with not null id and get response", async() => {
    const testMessage: string = `
    {
      "type" : "phrase_get_one",
      "data" : { "id": null }
    }
    `
    await sendMessage(testMessage)

    // Perform assertions on the response
    expect(responseMessage)
      .toEqual({
        "type": "phrase_get_one",
        "response": null
      })

    expect((responseMessage as WsResponse).response).toEqual(phrasesService.getPhraseById())
  })

  test("Send 'phrase_add_one' type message and get response", async() => {
    const addedId = 1_000_000
    const phrasesCountBeforeAdding = phrasesService.getPhrase().length

    const testMessage: string = `
    {
      "type" : "phrase_add_one",
      "data" : {
        "phrase": {
          "id" : ${ addedId },
          "title" : "added title",
          "text" : "added text"
        }
      }
    }
    `
    await sendMessage(testMessage, 100)

    // Perform assertions on the response
    expect(responseMessage).not.toEqual({})
    expect(responseMessage)
      .toEqual({
        "type": "phrase_add_one",
        "response": null
      })
    expect(phrasesService.getPhrase().length).toBe(phrasesCountBeforeAdding + 1)
  })

  test("Send 'phrase_update_one' type message and get response", async() => {
    const updateId = 4
    const phrasesCountBeforeUpdate = phrasesService.getPhrase().length

    const testMessage: string = `
    {
      "type" : "phrase_update_one",
      "data" : {
        "phrase": {
          "id" : ${ updateId },
          "title" : "updated title",
          "text" : "updated text"
        }
      }
    }
    `
    await sendMessage(testMessage, 100)

    // Perform assertions on the response
    expect(responseMessage).not.toEqual({})
    expect(responseMessage)
      .toEqual({
        "type": "phrase_update_one",
        "response": null
      })
    expect(phrasesService.getPhrase().length).toEqual(phrasesCountBeforeUpdate)
    expect(phrasesService.getPhraseById(4)).toEqual({"id": 4, "title": "updated title", "text": "updated text"})
  })
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
