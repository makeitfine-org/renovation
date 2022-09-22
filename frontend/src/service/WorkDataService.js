import {api} from "@/http-common";

class WorkDataService {
  getAll() {
    return api.get("/work");
  }

  get(id) {
    return api.get(`/work/${id}`);
  }

  findByTitle(title) {
    return api.get(`/work?title=${title}`);
  }

  create(data) {
    return api.post("/work", data);
  }

  update(id, data) {
    return api.patch(`/work/${id}`, data);
  }

  delete(id) {
    return api.delete(`/work/${id}`);
  }
}

export default new WorkDataService();
