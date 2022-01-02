import axios from "axios";

const backend_port = 8090

export default axios.create({
    baseURL: `http://localhost:${backend_port}/api`,
    headers: {
        "Content-type": "application/json"
    }
});
