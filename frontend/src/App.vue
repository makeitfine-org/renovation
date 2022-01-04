<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2022
  -->

<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <router-link to="/" class="navbar-brand">&nbsp; Renovation</router-link>
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <router-link :to="{name: 'home'}" class="nav-link">Home</router-link>
        </li>
        <li class="nav-item">
          <router-link :to="{name: 'work'}" class="nav-link">Work</router-link>
        </li>
      </div>
    </nav>

    <div class="container">
      <router-view/>
    </div>
  </div>
</template>
