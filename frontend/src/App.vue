<!--
  - Created under not commercial project "Renovation"
  -
  - Copyright 2021-2022
  -->

<template>
  <div id="app">
    <h1>Hi, it's renovation frontend module</h1>
    <hr>

    <router-link :to="{ name: 'home' }">Home</router-link>
    &nbsp;
    <router-link :to="{ name: 'work' }">Work</router-link>

    <div class="container">
      <router-view/>
    </div>
  </div>
</template>
