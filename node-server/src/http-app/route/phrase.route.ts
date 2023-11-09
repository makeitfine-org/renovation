/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Request, Response, Router} from "express"
import {PhraseService} from "../../data/service/phrase.service" //todo: route from src as default

const router = Router()

router.get("/", (req: Request, res: Response): void => {
  res.status(200).send(new PhraseService().getPhrase())//todo: single tor service
})

export {router}
