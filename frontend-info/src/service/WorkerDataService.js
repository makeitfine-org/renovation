import {backendApi, infoGraphql} from "@/http-common";

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
        return infoGraphql.post("", data);
    }

    getWorkers() {
        return infoGraphql.get("/api/v1/info");
        // return backendApi().get("/worker");
    }
}

export default new WorkerDataService();
