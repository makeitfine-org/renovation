import {backendApi, infoGraphql, localhost} from "@/http-common"

class WorkerDataService {
    getAllData() {
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
        // return infoGraphql.post("/graphql", data)
        return localhost.get("/graphql")
    }

    getWorkers() {
        // return backendApi.get("/worker")
        return localhost.get("/worker")
    }
}

export default new WorkerDataService()
