<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2023
  -->

<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <router-link to="/" class="navbar-brand">&nbsp; Renovation</router-link>
      <div class="container-fluid">
        <div class="navbar-nav mr-auto">
          <li class="nav-item">
            <router-link :to="{name: 'home'}" class="nav-link">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link :to="{name: 'workFacade'}" class="nav-link">Work</router-link>
          </li>
          <li class="nav-item">
            <router-link :to="{name: 'addWork'}" class="nav-link">Add Work</router-link>
          </li>
          <li class="nav-item">
            <router-link :to="{name: 'workerFacade'}" class="nav-link">Worker</router-link>
          </li>
        </div>

        <div class="d-flex align-items-center">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <button type="button" class="btn btn-light" @click="logout">Logout</button>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container">
      <router-view/>
    </div>
  </div>
</template>

<script>
import {root} from "@/http-common"

export default {
  name: "app",
  methods: {
    logout(delta) {
      root.get(`/logout-without-redirect`).then(response => {
        console.log(response.data)

        this.$router.go(delta)
      }).catch(e => {
        console.log(e)
      })
    }
  }
}
</script>
