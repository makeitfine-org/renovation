<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2022
  -->

<template>
  <li class="list-group-item row d-flex">
    <div class="list-item col-sm border-end border-1">
      {{ worker.name }}
    </div>
    <div class="list-item col-sm border-end border-1">
      {{ worker.surname }}
    </div>
    <div class="list-item col-sm">
      {{ worker.age }}
    </div>
  </li>
</template>

<script>
export default {
  props: {
    worker: {
      type: Object,
      required: true
    },
    idx: Number
  },
  methods: {
  }
}
</script>
