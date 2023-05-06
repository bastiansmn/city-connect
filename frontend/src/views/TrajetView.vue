<template>
   <div class="container">
      <LeftPanel @updateMarkers="updateMapMarkers"/>
      <MapWrapper class="right-panel" :markers="metros"/>
   </div>
</template>

<script setup lang="ts">
import LeftPanel from "@/components/LeftPanel.vue";
import MapWrapper from "@/components/MapWrapper.vue";
import {ref} from "vue";

const metros = ref([]);

function updateMapMarkers(metroData: any) {
   metros.value = metroData;
}
</script>
