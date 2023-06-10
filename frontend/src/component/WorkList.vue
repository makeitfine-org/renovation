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
          <!--          <div class="list-item col-sm border-end border-3">-->
          <!--            <strong>Id</strong>-->
          <!--          </div>-->
          <div class="list-item col-sm border-end border-3">
            <strong>Title</strong>
          </div>
          <!--          <div class="list-item col-sm border-end border-3">-->
          <!--            <strong>Description</strong>-->
          <!--          </div>-->
          <div class="list-item col-sm border-end border-3">
            <strong>Price</strong>
          </div>
          <!--          <div class="list-item col-sm border-end border-3">-->
          <!--            <strong>End date</strong>-->
          <!--          </div>-->
          <div class="list-item col-sm">
            <strong>Pay date</strong>
          </div>
        </li>

        <WorkItem :class="{active: idx === currentIndex}"
                  v-for="(work, idx) in works"
                  :key="idx"
                  :work="work"
                  :idx="idx"
                  @click="setActiveWork(work, idx)"
        />
      </ul>
    </div>
    <div class="col-md-5">
      <div v-if="currentWork">
        <h4>Work</h4>
        <div>
          <label><strong>Title:</strong></label> {{ currentWork.title }}
        </div>
        <div>
          <label><strong>Description:</strong></label> {{ currentWork.description }}
        </div>
        <div>
          <label><strong>Price:</strong></label> {{ currentWork.price }}
        </div>
        <div>
          <label><strong>End date:</strong></label> {{ currentWork.endDate }}
        </div>
        <div>
          <label><strong>Pay date:</strong></label> {{ currentWork.payDate }}
        </div>
        <div style="margin-top: 7px;">
          <button class="badge-light badge-success"
                  @click="editWork"
          >
            Edit
          </button>
        </div>
      </div>
      <div v-else>
        <br/>
        <p>Please click on a Work...</p>
      </div>
    </div>
  </div>
</template>

<script>
import WorkItem from "@/component/WorkItem"

export default {
  props: ['works'],
  components: {
    WorkItem
  },
  data: () => ({
    currentWork: null,
    currentIndex: -1
  }),
  methods: {
    setActiveWork(work, index) {
      this.currentWork = work;
      this.currentIndex = index;
    },
    editWork() {
      this.$router.push({name: 'work-details', params: {id: this.currentWork.id}})
    }
  }
}
</script>
