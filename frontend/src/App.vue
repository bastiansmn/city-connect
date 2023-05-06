<template>
   <van-nav-bar class="navbar">
      <template #left>
         <router-link class="link link--logo" to="/">
            <CityConnectLogo/>
         </router-link>
      </template>
      <template #right>
         <router-link to="/">Présentation</router-link>
         <router-link class="link" to="/trajets">Trajets</router-link>
         <router-link class="link" to="/horraires">Horaires</router-link>
      </template>
   </van-nav-bar>
   <router-view />
</template>
<script>
import {NavBar} from 'vant';
import {RouterLink} from 'vue-router';
import CityConnectLogo from "@/components/CityConnectLogo.vue";

export default {
   name: 'App',
   components: {
      CityConnectLogo,
      [NavBar.name]: NavBar,
      [RouterLink.name]: RouterLink
   }
}
</script>
<style lang="scss">
:root {
   --van-nav-bar-height: 80px !important;
}

@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@600&display=swap');

#app {
   font-family: 'Poppins', sans-serif;
   -webkit-font-smoothing: antialiased;
   -moz-osx-font-smoothing: grayscale;
   text-align: center;
   color: #2c3e50;
}

* {
   box-sizing: border-box;
   margin: 0;
}

.link {
   margin: 0 10px;
   display: grid;
   place-items: center;

   &.link--logo {
      @media screen and (min-width: 1000px) {
         display: block;
      }

      display: none;
   }
}

.container {
   display: flex;
   justify-content: space-between;
   flex-direction: column-reverse;


   @media screen and (min-width: 1000px) {
      flex-direction: row;
   }

   position: relative;
   align-items: flex-start;
   height: 100vh;
   width: 100vw;

   & .left-panel {
      background: transparent;
      position: absolute;
      bottom: 0;
      z-index: 100;
      box-sizing: border-box;
      flex: 1;
      padding: .8rem;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;

      width: 100%;
      max-width: 700px;

      & .wrapper {
         background: white;
         box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
         border-radius: 7px;
         padding: 1rem;
         box-sizing: border-box;

         width: 100%;
      }

      transition: all ease-in-out .4s;
      transform: translateY(0);

      &.closed {
         transform: translateY(80%);
      }
   }

   & .right-panel {
      position: fixed !important;
      overflow: hidden;
      z-index: 10;
      top: 0;
      box-sizing: border-box;
      flex: 2;
      height: 100%;
      width: 100%;
   }
}

.van-nav-bar {
   background-color: #F3F7F0 !important;
   position: fixed !important;
   top: 0;
   left: 0;
   width: 100%;
   z-index: 10000 !important;

   .van-nav-bar__title {
      font-weight: bold;
      color: #2c3e50;
      font-size: 25px;
   }

   .van-nav-bar__left,
   .van-nav-bar__right {
      a {
         font-size: 20px;
         font-weight: bold;
         color: #2c3e50;

         &.router-link-active {
            color: #7BC654;
         }
      }
   }
}
</style>