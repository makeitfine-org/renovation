<template>
  <div>
    <h1>😉 Work page 😉</h1>
    <div>
      <h4>Works List</h4>

      <Loading v-if="loading"/>

      <WorkList v-else-if="works.length"
                :works="works"/>
      <div v-else>No works</div>

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
    }
  },
  mounted() {
    this.retrieveWorks()
  }
}

</script>
