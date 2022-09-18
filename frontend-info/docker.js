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

let accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ6eFZ4RGx2UFE2dGFhYVdHTnRCMDE4bXk5VnhqbDBCRTlIZXY5RzBRWjJvIn0.eyJleHAiOjE2NjM1MTE4NDksImlhdCI6MTY2MzUxMDM0OSwianRpIjoiOTk0YmU2MGQtYzk1MS00MWIyLTlhZTUtZTlhN2FhYWVlMTRhIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDoxODA4MC9yZWFsbXMvcmVub3ZhdGlvbi1yZWFsbSIsInN1YiI6ImQyMDU4ODIxLTY2YWUtNDhlZC1iNWI0LWFkOGMzNTU4OWYzNyIsInR5cCI6IkJlYXJlciIsImF6cCI6InJlbm92YXRpb24tY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6ImEyYTc4Y2RmLWQ0MzQtNDdkZi05NmQ4LTc1MTZhZTdhMWZlOSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLXJlbm92YXRpb24iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlbm92YXRpb24tY2xpZW50Ijp7InJvbGVzIjpbIndvcmsiLCJhZG1pbiIsIndvcmtlciJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6ImEyYTc4Y2RmLWQ0MzQtNDdkZi05NmQ4LTc1MTZhZTdhMWZlOSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGwtdGVzdCJ9.NGFWYcyYxXPhtH1GoRdCg_6NRDNTGpbwYm9yODtkirFCrZAosMso70tfupkUIyEBwGgPwmhaVi3e3LpnyXoSoPeNOYPL8Ealbvol2BbI7kpUelJzme9DtSe1mhUmX2fqhWxZmRas44HNlPMeRO15uGF-PlRg4-qAu8dWk346T5ta0M0OxtLRT940pmXWyoITr1J1AGanL4hEko81RYF7_OUiNLPE-c1sEZ-StBNRmJ-kul9cS2itUqxzpMsvigc7Meeikp4htQevFO1LxSSm7EdgHefJUJGr8ym3pqrz8fMw9g9P-7ON74gES-lRmxqsP21M3v1Wny4U6YEXVlUHRw"

app.get('/worker', function (req, res) {
    let url = `${process.env.VUE_APP_BACKEND_API_URL}/worker`

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

const path = __dirname + '/dist'
app.use(express.static(path))
app.use('/', router)

const port = parseInt(process.env.PORT) || 8080
app.listen(port, function () {
    console.log(`Example app listening on port ${port}!`)
})
