<template>
  <div class="list row">
    <div class="col-md-8">
      <br>
      <div class="input-group mb-3">
        <input type="text" class="form-control" placeholder="Search by work"
               @keyup.enter="searchByTitle"
               v-model="title"/>
        <div class="input-group-append">
          <button class="btn btn-outline-secondary" type="button"
                  @click="searchByTitle"
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

        <WorkList v-else-if="allWorksCount"
                  :works="allWorks"/>
        <div v-else>No works</div>
      </div>
    </div>
  </div>
</template>

<script>
import Loading from "@/component/Loading"
import WorkList from "@/component/WorkList"
import {mapActions, mapGetters} from 'vuex'

export default {
  name: 'workFacade',
  data: () => ({
    title: ''
  }),
  components: {
    Loading,
    WorkList
  },
  computed: mapGetters(['allWorks', 'allWorksCount', 'loading']),
  methods: {
    ...mapActions(['retrieveWorks', 'searchWorksByTitle']),
    searchByTitle() {
      this.searchWorksByTitle(this.title)
      this.title = ''
    }
  },
  mounted() {
    this.retrieveWorks()
  }
}

</script>
