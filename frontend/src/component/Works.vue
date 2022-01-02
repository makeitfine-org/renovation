<template>
  <div>
    <h1>😉 Work page 😉</h1>

    <div class="col-md-6">
      <h4>Works List</h4>
      <ul class="list-group">
        <li class="list-group-item"
            v-for="(work) in works"
        >
          {{ work.title }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import workDataService from "@/service/WorkDataService";

export default {
  name: 'works',
  data() {
    return {
      works: [],
    }
  },
  methods: {
    retrieveWorks() {
      workDataService.getAll()
          .then(response => {
            this.works = response.data;
            console.log(response.data);
          })
          .catch(e => {
            console.log(e);
          });
    }
  },
  mounted() {
    this.retrieveWorks();
  }
}

</script>
