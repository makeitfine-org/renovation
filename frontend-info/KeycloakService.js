const axios = require('axios')

const tokenEndpoint = process.env.VUE_APP_TOKEN_ENDPOINT || 'http://localhost:18080/realms/renovation-realm/protocol/openid-connect/token'
const clientId = process.env.VUE_APP_CLIENT_ID || 'renovation-client'
const clientSecret = process.env.VUE_APP_CLIENT_SECRET || '341b3ff9-7af0-4854-b024-63a82e7174cd'
const username = process.env.VUE_APP_USERNAME || 'all-test'
const password = process.env.VUE_APP_PASSWORD || 'test'

async function getClientCredentialsGrantTypeAccessToken() {
    const params = new URLSearchParams()
    params.append('grant_type', 'client_credentials')
    params.append('client_id', clientId)
    params.append('client_secret', clientSecret)

    return grantTypeAccessToken(params)
}

async function getPasswordGrantTypeAccessToken() {
    const params = new URLSearchParams()
    params.append('grant_type', 'password')
    params.append('client_id', clientId)
    params.append('client_secret', clientSecret)
    params.append('username', username)
    params.append('password', password)

    return grantTypeAccessToken(params)
}

async function grantTypeAccessToken(params) {


    const config = {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }

    try {
        const res = await axios.post(tokenEndpoint, params, config);
        return res.data['access_token']
    } catch (e) {
        console.error(e);
    } finally {
        console.log('Getting token completed');
    }
}

module.exports = {
    getClientCredentialsGrantTypeAccessToken,
    getPasswordGrantTypeAccessToken
}
