<template>
  <div class="list row">
    <div class="col-md-12">
      <h1>😉 Worker page 😉</h1>
      <div>
        <h4>Workers List</h4>

        <Loading v-if="loading"/>

        <WorkerList v-else-if="allWorkersCount"
                  :workers="allWorkers"/>
        <div v-else>No workers</div>
      </div>
    </div>
  </div>
</template>

<script>
import Loading from "@/component/Loading"
import WorkerList from "@/component/worker/WorkerList"
import {mapActions, mapGetters} from 'vuex'

export default {
  name: 'workerFacade',
  data: () => ({
    title: ''
  }),
  components: {
    Loading,
    WorkerList
  },
  computed: mapGetters(['allWorkers', 'allWorkersCount', 'loading']),
  methods: {
    ...mapActions(['retrieveWorkers']),
  },
  mounted() {
    this.retrieveWorkers()
  }
}

</script>
