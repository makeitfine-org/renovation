/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

const express = require('express')
const app = express()
const router = express.Router()
const axios = require('axios')

// app.use(function(req, res, next) {
//     console.log('/' + req.method)
//     res.setHeader("Content-Type", "application/json")
//     res.setHeader('charset', 'utf-8')
//     next()
// });

router.get('/check', function (req, res) {
    let message = "<>Hello from frontend-info!<><br/>"

    message += `VUE_APP_BACKEND_API_URL: ${process.env.VUE_APP_BACKEND_API_URL} <br/>`
    message += `VUE_APP_INFO_GRAPHQL_URL: ${process.env.VUE_APP_INFO_GRAPHQL_URL} <br/>`
    message += `PORT: ${process.env.PORT}`

    return res.send(message)
})

app.get('/worker', function (req, res) {
    let url = `${process.env.VUE_APP_BACKEND_API_URL}/worker`

    axios({
        method: 'get',
        url
    })
        .then(function (response) {
            res.setHeader("Content-Type", "application/json")
            res.send(JSON.stringify(response.data))
        })
        .catch(function (error) {
            console.log(error)
        })
})

app.get('/graphql', function (req, res) {
    let url = `${process.env.VUE_APP_INFO_GRAPHQL_URL}/graphql`

    const data = {
        query: `
                {
                  details {
                    id
                    name
                    surname
                    age
                    gender
                  }
                }
            `
    }

    axios.post(url, data)
        .then(function (response) {
            res.setHeader("Content-Type", "application/json")
            res.send(JSON.stringify(response.data))
        })
        .catch(function (error) {
            console.log(error)
        })
})

const path = __dirname + '/dist'
app.use(express.static(path))
app.use('/', router)

const port = parseInt(process.env.PORT) || 8080
app.listen(port, function () {
    console.log(`Example app listening on port ${port}!`)
})
