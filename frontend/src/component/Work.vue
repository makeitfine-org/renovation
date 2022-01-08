<template>
  <div v-if="currentWork" class="edit-form">
    <h4>Work</h4>
    <form>
      <div class="form-group">
        <label for="title">Title</label>
        <input type="text" class="form-control" id="title"
               v-model="currentWork.title"
        />
      </div>
      <div class="form-group">
        <label for="description">Description</label>
        <input type="text" class="form-control" id="description"
               v-model="currentWork.description"
        />
      </div>
      <div class="form-group">
        <label for="price">Price</label>
        <input type="number" class="form-control" id="price"
               v-model="currentWork.price"
        />
      </div>
      <div class="form-group">
        <label for="endDate">End date</label>
        <input type="text" class="form-control" id="endDate"
               v-model="currentWork.endDate"
        />
      </div>
      <div class="form-group">
        <label for="payDate">Pay date</label>
        <input type="text" class="form-control" id="payDate"
               v-model="currentWork.payDate"
        />
      </div>

    </form>
    <div style="margin-top: 7px">
      <button class="badge badge-danger mr-2"
              @click="deleteWork"
      >
        Delete
      </button>
      &nbsp;
      <button type="submit" class="badge badge-success"
              @click="updateWork"
      >
        Update
      </button>
      &nbsp;
      <button type="button" class="badge badge-success"
              @click="this.$router.push({ name: 'workFacade' })"
      >
        Cancel
      </button>
    </div>
    <p>{{ message }}</p>
  </div>

  <div v-else>
    <br/>
    <p>Please click on a Work...</p>
  </div>
</template>

<script>
import workDataService from "@/service/WorkDataService";

export default {
  name: "work",
  data: () => ({
    currentWork: null,
    message: ''
  }),
  methods: {
    getWork(id) {
      workDataService.get(id)
          .then(response => {
            this.currentWork = response.data;
            console.log(response.data);
          })
          .catch(e => {
            console.log(e);
          });
    },

    updateWork() {
      //todo: this.currentWork.id is null cause work/{id} return DTO without id
      workDataService.update(this.currentWork.id, this.currentWork)
          .then(response => {
            console.log(response.data);
            this.message = 'The work was updated successfully!';
          })
          .catch(e => {
            console.log(e);
          });
    },

    deleteWork() {
      workDataService.delete(this.currentWork.id)
          .then(response => {
            console.log(response.data);
            this.$router.push({name: 'workFacade'});
          })
          .catch(e => {
            console.log(e);
          });
    }
  },
  mounted() {
    this.message = '';
    this.getWork(this.$route.params.id);
  }
};
</script>
