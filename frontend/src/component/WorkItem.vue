<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2023
  -->

<template>
  <li class="list-group-item row d-flex"
      @dblclick="handleItemClick(work.id)"
  >
<!--    <div class="list-item col-sm border-end border-1">-->
<!--      <strong>{{ idx }}</strong>-->
<!--    </div>-->
    <div class="list-item col-sm border-end border-1">
      {{ work.title }}
    </div>
<!--    <div class="list-item col-sm border-end border-1">-->
<!--      {{ work.description }}-->
<!--    </div>-->
    <div class="list-item col-sm border-end border-1">
      {{ work.price }}
    </div>
<!--    <div class="list-item col-sm border-end border-1">-->
<!--      {{ work.endDate }}-->
<!--    </div>-->
    <div class="list-item col-sm">
      {{ work.payDate }}
    </div>
  </li>
</template>

<script>
export default {
  props: {
    work: {
      type: Object,
      required: true
    },
    idx: Number
  },
  methods: {
    handleItemClick(id) {
      this.$router.push({name: 'work-details', params: {id: id}})
    }
  }
}
</script>
