<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2022
  -->

<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-primary">
      <router-link to="/" class="navbar-brand">&nbsp; Renovation info</router-link>
      <div class="navbar-nav mr-auto">
<!--        <li class="nav-item">-->
<!--          <router-link :to="{name: 'home'}" class="nav-link">Home</router-link>-->
<!--        </li>-->
<!--        <li class="nav-item">-->
<!--          <router-link :to="{name: 'workFacade'}" class="nav-link">Work</router-link>-->
<!--        </li>-->
<!--        <li class="nav-item">-->
<!--          <router-link :to="{name: 'addWork'}" class="nav-link">Add Work</router-link>-->
<!--        </li>-->
        <li class="nav-item">
          <router-link :to="{name: 'workerFacade'}" class="nav-link">Worker</router-link>
        </li>
      </div>
    </nav>

    <div class="container">
      <router-view/>
    </div>
  </div>
</template>

<script>
export default {
  name: "app"
};
</script>
