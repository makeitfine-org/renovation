/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

import {Router} from 'express'

const router = Router()

router.get('/hi', (req, res) => res.send('<h1>Hi!</h1>'))

export default router
