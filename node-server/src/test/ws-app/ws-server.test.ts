/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import * as http from "http"
import {WsServerFactory} from "../../main/ws-app/ws-server-factory"
import WebSocket from "ws"

describe("WebSocket Server", () => {
  const DEFAULT_TEST_PORT = 3111
  const port = DEFAULT_TEST_PORT
  let server: http.Server

  beforeAll(async() => {
    // Start server
    server = await startServer(port)
  })

  afterAll(() => {
    // Close server
    server.close()
  })

  test("Server echoes the message it receives from client", async() => {
    // Create test client
    const client = new WebSocket(`ws://localhost:${ port }`)
    await waitForSocketState(client, client.OPEN)

    const testMessage: string = `
    {
      "type" : "other",
      "data" : {
                  "message":"hello"
               }
    }
    `
    let responseMessage: object | null = null

    // @ts-ignore
    client.on("message", (data) => {
      responseMessage = JSON.parse(data.toString())

      // Close the client after it receives the response
      client.close()
    })

    // Send client message
    client.send(testMessage)

    // Perform assertions on the response
    await waitForSocketState(client, client.CLOSED)
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
    }, 5)
  })
}
