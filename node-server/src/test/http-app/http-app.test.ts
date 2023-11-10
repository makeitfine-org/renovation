/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

// @ts-ignore
import request from "supertest"
// @ts-ignore
import app from "@main/http-app/http-app"

describe("http-app.ts", () => {

  test("/", async() => {
    const res = await request(app).get("/")
    expect(res.text).toEqual("Hello World!!!")
    expect(res.status).toEqual(200)
  })

  test("/module", async() => {
    const res = await request(app).get("/module")
    expect(res.body).toEqual({message: "'node-server' module (node js based) of renovation project"})
    expect(res.status).toEqual(200)
  })

  test("/phrase", async() => {
    const res = await request(app).get("/phrase")
    expect(res.body).toEqual(
      [ {id: 1, title: "hi", text: "Hello everyone!"},
        {id: 2, title: "place", text: "I'm here!"},
        {id: 3, title: "question", text: "Who there?"},
        {id: 4, title: "bed words", text: "Damn!"},
        {id: 5, title: "quite", text: "I'm off"}
      ])
    expect(res.status).toEqual(200)
  })

  test("/phrase not equals", async() => {
    const res = await request(app).get("/phrase")
    expect(res.body).not.toEqual(
      [ {id: 1, title: "hi", text: "Hello everyone!"},
        {id: 2, title: "place", text: "I'm here!"},
        {id: 4, title: "bed words", text: "Damn!"},
        {id: 3, title: "question", text: "Who there?"},
        {id: 5, title: "quite", text: "I'm off"}
      ])
    expect(res.status).toEqual(200)
  })

  test("/nopage not equals", async() => {
    const res = await request(app).get("/nopage")
    expect(res.body).toEqual({})
    expect(res.status).toEqual(404)
  })
})
