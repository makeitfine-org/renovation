<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2022
  -->

<template>
  <div>
    <ul class="list-group">
      <li class="list-group-item row d-flex border-3">
        <div class="list-item col-sm border-end border-3">
          <strong>Id</strong>
        </div>
        <div class="list-item col-sm border-end border-3">
          <strong>Title</strong>
        </div>
        <div class="list-item col-sm border-end border-3">
          <strong>Description</strong>
        </div>
        <div class="list-item col-sm border-end border-3">
          <strong>Price</strong>
        </div>
        <div class="list-item col-sm border-end border-3">
          <strong>End date</strong>
        </div>
        <div class="list-item col-sm">
          <strong>Pay date</strong>
        </div>
      </li>

      <WorkItem v-for="(work, idx) in works"
                :work="work"
                :idx="idx"
      />
    </ul>
  </div>
</template>

<script>
import WorkItem from "@/component/WorkItem"

export default {
  props: ['works'],
  components: {
    WorkItem
  },
}
</script>
