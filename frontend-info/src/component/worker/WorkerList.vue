<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2023
  -->

<template>
  <div class="list row">
    <div class="col-md-7">
      <ul class="list-group">
        <li class="list-group-item row d-flex border-3">
          <div class="list-item col-sm border-end border-3">
            <strong>Name</strong>
          </div>
          <div class="list-item col-sm border-end border-3">
            <strong>Surname</strong>
          </div>
          <div class="list-item col-sm">
            <strong>Age</strong>
          </div>
        </li>

        <WorkerItem :class="{active: idx === currentIndex}"
                  v-for="(worker, idx) in workers"
                  :key="idx"
                  :worker="worker"
                  :idx="idx"
                  @click="setActiveWorker(worker, idx)"
        />
      </ul>
    </div>
    <div class="col-md-5">
      <div v-if="currentWorker">
        <h4>Worker</h4>
        <div>
          <label><strong>Id:</strong></label> {{ currentWorker.id }}
        </div>
        <div>
          <label><strong>Name:</strong></label> {{ currentWorker.name }}
        </div>
        <div>
          <label><strong>Surname:</strong></label> {{ currentWorker.surname }}
        </div>
        <div>
          <label><strong>Age:</strong></label> {{ currentWorker.age }}
        </div>
        <div>
          <label><strong>Gender:</strong></label> {{ currentWorker.gender }}
        </div>
<!--        <div style="margin-top: 7px;">-->
<!--          <button class="badge-light badge-success"-->
<!--                  @click="editWork"-->
<!--          >-->
<!--            Edit-->
<!--          </button>-->
<!--        </div>-->
      </div>
      <div v-else>
        <br/>
        <p>Please click on a Worker...</p>
      </div>
    </div>
  </div>
</template>

<script>
import WorkerItem from "@/component/worker/WorkerItem"

export default {
  props: ['workers'],
  components: {
    WorkerItem
  },
  data: () => ({
    currentWorker: null,
    currentIndex: -1
  }),
  methods: {
    setActiveWorker(worker, index) {
      this.currentWorker = worker;
      this.currentIndex = index;
    },
    // editWork() {
    //   this.$router.push({name: 'work-details', params: {id: this.currentWork.id}})
    // }
  }
}
</script>
