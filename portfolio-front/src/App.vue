<template>
  <nav>
    <router-link to="/">Accueil</router-link> |
    <router-link to="/admin">Admin</router-link>
  </nav>

  <router-view />
</template>

<style>
/* CSS global très basique */
body {
  font-family: sans-serif;
  margin: 0;
  padding: 20px;
  background: #f4f4f4;
}
nav {
  margin-bottom: 20px;
}
</style>
