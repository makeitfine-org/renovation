import axios from "axios";

const backendApiUrl = import.meta.env.VITE_BACKEND_API_URL || '/api'

const axiosCreate = (url) => axios.create({
    baseURL: url,
    headers: {
        "Content-type": "application/json"
    }
});

export const root = axiosCreate('/')

export const api = axiosCreate(backendApiUrl)
