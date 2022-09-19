/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

const express = require('express')
const app = express()
const router = express.Router()
const axios = require('axios')
const {getClientCredentialsGrantTypeAccessToken, getPasswordGrantTypeAccessToken} = require("./KeycloakService");

router.get('/check', function (req, res) {
    let message = "<>Hello from frontend-info!<><br/>"

    message += `VUE_APP_BACKEND_API_URL: ${process.env.VUE_APP_BACKEND_API_URL} <br/>`
    message += `VUE_APP_INFO_GRAPHQL_URL: ${process.env.VUE_APP_INFO_GRAPHQL_URL} <br/>`
    message += `PORT: ${process.env.PORT}`

    return res.send(message)
})

//todo: method should be removed cause of insecure
app.get('/insecure/token/grant/password', function (req, res) {

    getPasswordGrantTypeAccessToken().then(function (accessToken) {
        res.setHeader("Content-Type", "text/plain")
        res.send(accessToken)
    }).catch(function (error) {
        console.log(error)
    })
})

//todo: method should be removed cause of insecure
app.get('/insecure/token/grant/client', function (req, res) {

    getClientCredentialsGrantTypeAccessToken().then(function (accessToken) {
        res.setHeader("Content-Type", "text/plain")
        res.send(accessToken)
    }).catch(function (error) {
        console.log(error)
    })
})

app.get('/worker', function (req, res) {
    let url = `${process.env.VUE_APP_BACKEND_API_URL}/worker`

    getPasswordGrantTypeAccessToken().then(function (accessToken) {
        axios({
            method: 'get',
            headers: {"Authorization": `Bearer ${accessToken}`},
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
})

app.get('/graphql', function (req, res) {
    let url = `${process.env.VUE_APP_INFO_GRAPHQL_URL}/graphql`

    getPasswordGrantTypeAccessToken().then(function (accessToken) {
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

        axios.post(url, data, {
            headers: {"Authorization": `Bearer ${accessToken}`},
        })
            .then(function (response) {
                res.setHeader("Content-Type", "application/json")
                res.send(JSON.stringify(response.data))
            })
            .catch(function (error) {
                console.log(error)
            })
    })
})

const path = __dirname + '/dist'
app.use(express.static(path))
app.use('/', router)

const port = parseInt(process.env.PORT) || 8080
app.listen(port, function () {
    console.log(`Example app listening on port ${port}!`)
})
