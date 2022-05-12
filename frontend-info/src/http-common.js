import axios from "axios"

// const backendApiUrl = process.env.VUE_APP_BACKEND_API_URL || '/api'
// const infoGraphqlUrl = process.env.VUE_APP_INFO_GRAPHQL_URL

const axiosCreate = (url) => axios.create({
    baseURL: url,
    headers: {
        "Content-type": "application/json"
    }
})

// export const backendApi = axiosCreate(backendApiUrl)
// export const infoGraphql = axiosCreate(infoGraphqlUrl)
export const localhost = axiosCreate('')
