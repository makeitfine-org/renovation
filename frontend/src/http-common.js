import axios from "axios";


const backendApiUrl = process.env.VUE_APP_BACKEND_API_URL || '/api'

export default axios.create({
    baseURL: backendApiUrl,
    headers: {
        "Content-type": "application/json"
    }
});
