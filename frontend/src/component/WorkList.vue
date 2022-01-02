<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2022
  -->

<template>
  <div>
    <ul class="list-group">
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
