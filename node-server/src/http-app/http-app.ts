/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import express, {Application, Request, Response} from "express"

import {router as phraseRoute} from "./route/phrase.route"

const app: Application = express()

app.use("/phrase", phraseRoute)

app.get("/", (_, res) => {
  res.send("Hello World!")
})

app.use("/module", (req: Request, res: Response): void => {
  res.json({message: "'node-server' module (node js based) of renovation project"})
})

export default app
