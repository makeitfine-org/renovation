/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

import express from 'express'
import chalk from 'chalk'
import {dirname} from 'path';
import routes from './app/routes/index.js'

const publicPath = 'app/public'

const app = express()

app.use(express.static(publicPath))
app.use(express.json())
app.use(express.urlencoded({extended: false}))
app.use('/api', routes)


const port = process.env.PORT || 8080
app.listen(port, () => {
    console.log(
        chalk.rgb(255, 32, 0)
        ('Server is running on port: ', port, '...')
    )
})
