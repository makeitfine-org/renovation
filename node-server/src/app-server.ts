/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import express from "express"

const app = express()
const port = process.env.PORT || 3000

app.get("/", (_, res) => {
  res.send("Hello World!")
})

app.listen(port, () => {
  return console.log(`Express (typescript based) is listening at :${ port }`)
})

/* eslint-disable  @typescript-eslint/no-unused-vars *///todo: can be deleted
