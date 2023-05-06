<template>
    <div class="container">
        <HorrairePanel/>
        <MapWrapper class="right-panel"/>
    </div>
</template>

<script setup lang="ts">
import HorrairePanel from "@/components/HorrairePanel.vue";
import MapWrapper from "@/components/MapWrapper.vue";
</script>

<style scoped lang="scss">
.container {
   display: flex;
   flex-direction: column !important;

   @media screen and (min-width: 1000px) {
      flex-direction: row !important;
   }

   width: 100vw !important;
   height: 100vh !important;

   .left-panel {
      position: relative !important;

      @media screen and (min-width: 1000px) {
         width: 40% !important;
         height: 100% !important;
      }

      width: 100% !important;
      height: 70% !important;
      padding: var(--van-nav-bar-height) 1rem 1rem 1rem !important;
      max-width: unset !important;
      overflow-y: auto !important;
   }

   .right-panel {
      position: relative !important;

      @media screen and (min-width: 1000px) {
         width: 60% !important;
         height: 100% !important;
      }

      width: 100% !important;
      height: 30% !important;
      flex: unset !important;
   }
}
</style>