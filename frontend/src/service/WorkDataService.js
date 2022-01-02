import http from "@/http-common";

class WorkDataService {
  getAll() {
    return http.get("/work");
  }

  get(id) {
    return http.get(`/work/${id}`);
  }

  create(data) {
    return http.post("/work", data);
  }

  update(id, data) {
    return http.patch(`/work/${id}`, data);
  }

  delete(id) {
    return http.delete(`/work/${id}`);
  }
}

export default new WorkDataService();
