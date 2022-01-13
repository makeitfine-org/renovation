<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2022
  -->

<template>
  <div class="submit-form">
    <div v-if="!submitted">
      <div class="form-group">
        <label for="title">Title</label>
        <input
            type="text"
            class="form-control"
            id="title"
            required
            v-model="work.title"
            name="title"
        />
      </div>

      <div class="form-group">
        <label for="description">Description</label>
        <input
            class="form-control"
            id="description"
            required
            v-model="work.description"
            name="description"
        />
      </div>

      <div class="form-group">
        <label for="price">Price</label>
        <input
            class="form-control"
            id="price"
            required
            v-model="work.price"
            name="price"
        />
      </div>

      <div class="form-group">
        <label for="endDate">End Date</label>
        <input
            class="form-control"
            id="endDate"
            required
            v-model="work.endDate"
            name="endDate"
        />
      </div>

      <div class="form-group">
        <label for="payDate">Pay Date</label>
        <input
            class="form-control"
            id="payDate"
            required
            v-model="work.payDate"
            name="payDate"
        />
      </div>

      <div style="margin-top: 7px;">
        <button @click="saveWork" class="btn btn-success">Submit</button>
      </div>
    </div>

    <div v-else>
      <h4>You submitted successfully!</h4>
      <button class="btn btn-success" @click="newWork">Add</button>
    </div>
  </div>
</template>

<script>
import workDataService from "@/service/WorkDataService"

export default {
  name: 'add-work',

  data: () => ({
    work: {
      id: null,
      title: '',
      price: null,
      endDate: null,
      payDate: null
    },
    submitted: false
  }),
  methods: {
    saveWork() {
      const data = {
        title: this.work.title,
        description: this.work.description,
        price: this.work.price,
        endDate: this.work.endDate,
        payDate: this.work.payDate,
      };

      workDataService.create(data)
          .then(response => {
            this.work.id = response.data.id;
            console.log(response.data);
            this.submitted = true;
          })
          .catch(e => {
            console.log(e);
          });
    },

    newWork() {
      this.submitted = false;
      this.work = {};
    }
  }
}

</script>
