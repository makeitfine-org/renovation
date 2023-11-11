/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Request, Response, Router} from "express"
import {PhraseService} from "@main/data/service/phrase.service"

const router = Router()

router.get("/", (_: Request, res: Response) =>
  res
    .send(PhraseService.getInstance().getPhrase())
    .status(200)
)

export {router}
