import http from "@/http-common";

class WorkerDataService {
  getAll() {
    return http.get("/worker");
  }

  // get(id) {
  //   return http.get(`/worker/${id}`);
  // }

  // findByTitle(title) {
  //   return http.get(`/worker?title=${title}`);
  // }

  // create(data) {
  //   return http.post("/work", data);
  // }
  //
  // update(id, data) {
  //   return http.patch(`/work/${id}`, data);
  // }
  //
  // delete(id) {
  //   return http.delete(`/work/${id}`);
  // }
}

export default new WorkerDataService();
