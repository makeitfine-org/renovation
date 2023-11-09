/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Request, Response, Router} from "express"
import {PhraseService} from "../../data/service/phrase.service" //todo: route from src as default

const router = Router()

router.get("/", (_: Request, res: Response) =>
  res
    .send(PhraseService.getInstance().getPhrase())
    .status(200)
)

export {router}
