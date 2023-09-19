<template>
      <l-map
            id="map"
            ref="map"
            class="map"
            v-model:zoom="zoom"
            :center="[mapLat, mapLng]"
            :bounds="bounds"
            :max-bounds="maxBounds"
            :min-zoom="minZoom"
            :max-zoom="maxZoom"
            :zoom-control="false"
            @click="addMarker"
      >
         <l-tile-layer
               url="/tiles/{z}/{x}/{y}.png"
               layer-type="base"
               name="OpenStreetMap"
         ></l-tile-layer>
         <l-polyline
             v-for="(coord, index) in polylinePath.slice(0, -1)"
             :key="index"
             :lat-lngs="[polylinePath[index], polylinePath[index + 1]]"
             :color="getPolylineColor(index)"
             :weight="8"
         ></l-polyline>
         <l-marker
               :key="marker"
               v-for="marker in markers"
               :lat-lng="[marker.position.lat, marker.position.lng]"
         ></l-marker>
      </l-map>
</template>

<script setup lang="ts">
import {computed, ref} from "vue";
import { latLngBounds } from "leaflet";
import {LMap, LMarker, LTileLayer, LPolyline} from "@vue-leaflet/vue-leaflet";
import L from 'leaflet';
import {useMapStore} from "@/store/map.store";
import {Marker} from "@/model/marker.model";

const mapStore = useMapStore();

const bounds = ref(latLngBounds([
   [48.77, 2.2],
   [48.95, 2.5]
]));
const maxBounds = ref(latLngBounds([
   [48.77, 2.2],
   [48.95, 2.5]
]));
const minZoom = ref(12);
const maxZoom = ref(16);


function getPolylineColor(index: number) {
    if(index == polylineColor.value.length - 2 && polylineColor.value[index+1]!=undefined) {
        return polylineColor.value[index+1];
    }else if( polylineColor.value[index] == undefined){
       return polylineColor.value[index-1];
    }
    return polylineColor.value[index];
}

const markers = computed(() => mapStore.markers);
const polylinePath = computed(() => mapStore.polylines.latlngs);

mapStore.mapLat = 48.858687;
mapStore.mapLng = 2.344563;

const mapLat = computed(() => mapStore.mapLat);
const mapLng = computed(() => mapStore.mapLng);

const polylineColor = computed(() => mapStore.polylines.color);


const zoom = ref(13);

let i = 0;

async function addMarker(customEvent: L.LeafletMouseEvent) {
   try {
      const latLng = customEvent.latlng;

      if (!latLng) return;

      // Attendre que latLng.lat et latLng.lng soient disponibles
      await Promise.resolve();

      const lat = latLng.lat;
      const lng = latLng.lng;

      const marker = {
         position: {lat, lng},
         draggable: false,
         title: "London"
      } as Marker;

      if(i === 0) {
         mapStore.markers = [];
         mapStore.markers.push(marker);
         mapStore.depart = lat+" "+lng;
          mapStore.polylines = {
              latlngs: [],
              color: []
          };
         i = i + 1;
      }else if(i===1){
         mapStore.markers.push(marker);
         mapStore.arrive = lat+" "+lng;
         i = 0;
      }else{
         mapStore.markers = [];
         mapStore.arrive="";
         mapStore.depart="";
         i=0;
      }

   } catch (e) {
      console.error(e);
   }

   // Ajouter votre logique de marqueur ici
   // Créer un nouveau marqueur à la position cliquée
}






</script>

<style scoped>
.map {
   height: 100vh;
   width: 100vw;
   position: relative;
}
.leaflet-control-zoom {
    display: none;
}
</style>