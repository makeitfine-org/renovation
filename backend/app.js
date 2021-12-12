/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

import express from 'express'
import chalk from 'chalk'

const app = express()
app.use(express.static('public'))

const port = process.env.PORT || 8080
app.listen(port, () => {
    console.log(
        chalk.rgb(255, 32, 0)
        ('Server is running on port: ', port, '...')
    )
})
