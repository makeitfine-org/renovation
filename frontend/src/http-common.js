import axios from "axios";

const backendApiUrl = process.env.VUE_APP_BACKEND_API_URL || '/api'

const axiosCreate = (url) => axios.create({
    baseURL: url,
    headers: {
        "Content-type": "application/json"
    }
});

export const root = axiosCreate('/')

export const api = axiosCreate(backendApiUrl)
