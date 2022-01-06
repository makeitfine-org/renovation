<template>
  <div class="list row">
    <div class="col-md-8">
      <br>
      <div class="input-group mb-3">
        <input type="text" class="form-control" placeholder="Search by work"
               @keyup.enter="searchTitle"
               v-model="title"/>
        <div class="input-group-append">
          <button class="btn btn-outline-secondary" type="button"
                  @click="searchTitle"
          >
            Search
          </button>
        </div>
      </div>
    </div>
    <div class="col-md-12">
      <h1>😉 Work page 😉</h1>
      <div>
        <h4>Works List</h4>

        <Loading v-if="loading"/>

        <WorkList v-else-if="works.length"
                  :works="works"/>
        <div v-else>No works</div>

      </div>
    </div>
  </div>
</template>

<script>
import Loading from "@/component/Loading"
import WorkList from "@/component/WorkList"
import workDataService from "@/service/WorkDataService"

export default {
  name: 'works',
  data: () => ({
    works: [],
    loading: true,
  }),
  components: {
    Loading,
    WorkList
  },
  methods: {
    retrieveWorks() {
      workDataService.getAll()
          .then(response => {
            this.works = response.data
            console.log(response.data)
            this.loading = false
          })
          .catch(e => {
            console.log(e)
          })
    },
    searchTitle() {
      workDataService.findByTitle(this.title)
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
    this.retrieveWorks()
  }
}

</script>
