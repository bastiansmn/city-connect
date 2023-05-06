<template>
   <div class="left-panel" :class="!inputsVisible ? 'closed' : ''">
      <div class="wrapper">
         <div>
            <h3 class="titleM">Veuillez choisir deux métros :</h3>
         </div>
         <div class="input-container">
            <div class="input-field">
               <van-field
                     class="custom-field"
                     list="first-station"
                     rows="3"
                     v-model="metro1Local"
                     name="Username"
                     autocomplete="off"
                     placeholder="Départ"
                     :rules="[{ required: true, message: 'Ce champ est obligatoire' }]"
                     @input="updateStoreDepart"
               />
               <div class="autocomplete" v-if="autocompleteMetro1.length > 0" id="first-station">
                  <span @click="fillInput1(station)" :key="station" v-for="station in autocompleteMetro1">{{ station }}</span>
               </div>
            </div>

            <div class="input-field">
               <van-field
                     class="custom-field"
                     list="second-station"
                     rows="3"
                     v-model="metro2Local"
                     name="Username"
                     autocomplete="off"
                     placeholder="Arrivée"
                     :rules="[{ required: true, message: 'Ce champ est obligatoire' }]"
                     @input="updateStoreArrivee"
               />
               <div class="autocomplete" v-if="autocompleteMetro2.length > 0" id="second-station">
                  <span @click="fillInput2(station)" :key="station" v-for="station in autocompleteMetro2">{{ station }}</span>
               </div>
            </div>

            <van-button class="button" type="success" @click="sendMetros">Confirmer</van-button>
         </div>
         <div class="trajet">
            <h3 class="titleM">Votre Trajet :</h3>
            <div class="station-list">
               <van-cell-group>
                  <van-cell
                        v-for="(walkTime, index) in walkData"
                        :key="index"
                        :title="'A pied:     '+walkTime + ' min'"
                  >
                     <template #icon>
                        <img src="../assets/walk.svg" alt="Walking" class="walk"/>
                     </template>
                  </van-cell>
               </van-cell-group>
               <van-cell-group>
                  <van-cell
                        v-for="(station, index) in data"
                        :key="index"
                        :title="station.name"
                  >
                     <template #icon>
                        <van-icon
                              class="metro-line"
                              :style="{ '--metro-color': getColor(station.ligne)}"
                        >
                           {{ station.ligne }}
                        </van-icon>
                     </template>
                  </van-cell>


               </van-cell-group>
            </div>
         </div>
      </div>
   </div>
</template>

<script setup lang="ts">
import {ref, watch} from "vue";
import {Marker} from "@/model/marker.model";
import {useMapStore} from '@/store/map.store'
import {Polyline} from "@/model/polyline.model";

const data = ref([]);

const walkData = ref([]);

const store = useMapStore();

const metro1 = computed<string>(() => {
   return store.depart;
});

const inputsVisible = ref(true);

const autocompleteMetro1 = ref<string[]>([]);
const autocompleteMetro2 = ref<string[]>([]);

const metro1Local = ref(metro1.value);

const fillInput1 = (station: string) => {
   metro1Local.value = station;
   autocompleteMetro1.value = [];
};

const fillInput2 = (station: string) => {
   metro2Local.value = station;
   autocompleteMetro2.value = [];
};

watch(metro1, (newValue) => {
   metro1Local.value = newValue;
   data.value = [];
   walkData.value = [];
});

const metro2 = computed(() => {
   return store.arrive;
});

const metro2Local = ref(metro2.value);

watch(metro2, (newValue) => {
   metro2Local.value = newValue;
   //split metro2 by space
   sendWalk();
});

store.markers = [] as Marker[];


function updateStoreDepart() {
   if (metro1Local.value.trim() === "") {
      autocompleteMetro1.value = [];
      return;
   }
   autocompleteStation(metro1Local.value).then((stations) => {
      if (stations.length > 0) {
         autocompleteMetro1.value = stations;
      } else {
         autocompleteMetro1.value = [];
      }
   });
   store.depart = metro1Local.value;
}

function updateStoreArrivee() {
   if (metro2Local.value.trim() === "") {
      autocompleteMetro2.value = [];
      return;
   }
   autocompleteStation(metro2Local.value).then((stations) => {
      if (stations.length > 0) {
         autocompleteMetro2.value = stations;
      } else {
         autocompleteMetro2.value = [];
      }
   });
   store.arrive = metro2Local.value;
}

async function sendWalk() {
   const reponse = await fetch(
       `/api/walk?latLng1=${store.depart}&latLng2=${store.arrive}`
   );
   walkData.value = [await reponse.json()];



}

async function autocompleteStation(station: string): Promise<string[]> {
   const response = await fetch(`/api/autocomplete?station=${station}`);
   return await response.json();
}

function getColor(ligne: string) {
   if (ligne == "7B"){
      return store.colors[14];
   }else if (ligne == "3B"){
      return store.colors[15];
   }else{
      const color = store.colors[Math.min(store.colors.length - 1, +ligne - 1)];
      return color;
   }
}

async function sendMetros() {
   const response = await fetch(
       `/api/metro?metro1=${metro1Local.value}&metro2=${metro2Local.value}`
   );
   data.value = await response.json();

   inputsVisible.value = false;

   const markers = (data.value.map((item: {
      lat: string | number;
      lon: string | number;
      name: string;
   }) => ({
      position: {lat: +item.lon, lng: +item.lat},
      title: item.name,
      draggable: false,
   } as Marker)));


   store.markers = [markers[0], markers[markers.length - 1]];

   const latlng1 = markers[0].position.lat + " " + markers[0].position.lng;
   const latlng2 = markers[markers.length - 1].position.lat + " " + markers[markers.length - 1].position.lng;

   const response2 = await fetch(
       `/api/walk?latLng1=${latlng1}&latLng2=${latlng2}`
   );

   walkData.value = [await response2.json()];

   store.mapLat = markers[0].position.lat;
   store.mapLng = markers[0].position.lng;

   const lines = data.value.map((item: {
      lat: string | number;
      lon: string | number;
      name: string;
      ligne: string | number;
   }, index: number) => {
      var colorToUse: string | number;
      //console.log("index " + index);
      if(index == 0 && item.ligne != "7B" && item.ligne != "3B" && item.ligne != "AP"){
         colorToUse = Math.min(store.colors.length - 1, +item.ligne - 1);
      }else if (item.ligne == "7B") {
         colorToUse = 14;
      } else if (item.ligne == "3B") {
         colorToUse = 15;
      }else if (item.ligne == "AP") {
         colorToUse = 16;
      } else {
            const previousItem = index < data.value.length-1 ? data.value[index + 1] : null; // Récupérer l'élément précédent s'il existe
            const previousLigne = previousItem ? previousItem.ligne : null; // Récupérer la valeur de `ligne` pour l'élément précédent
            colorToUse = Math.min(store.colors.length - 1, +previousLigne - 1)
      }


      return {
         lat: +item.lat,
         lng: +item.lon,
         colorIndex: colorToUse, // Utiliser la couleur correspondante
         name: item.name,
         ligne: item.ligne
      };
   });

   const polylines: Polyline = {
      // lines or red

      latlngs: lines.map((line) => [line.lng, line.lat]),
      color: lines.map((line) => store.colors[line.colorIndex]),
   };



   store.polylines = polylines;


}
</script>

<script lang="ts">
import {computed, defineComponent} from "vue";
import {Cell, CellGroup, Button, Field, Icon} from 'vant';

export default defineComponent({
   name: "LeftPanel",
   components: {
      VanButton: Button,
      VanField: Field,
      VanCell: Cell,
      VanCellGroup: CellGroup,
      VanIcon: Icon,
   },
});


</script>

<style scoped lang="scss">
.custom-field {
   border: 1px solid black;
}

.titleM {
   margin-top: 20px;
   margin-bottom: 40px;
}

.logo-container {
   display: flex;
   align-items: center;
   justify-content: center;
}

.logo-container img {
   width: 50%;
   height: auto;
}

.logo-container h1 {
   margin-left: 10px;
   font-size: 24px;
   font-weight: 600;
}

.input-container {
   margin-top: 20px;
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 10px;

   & > .input-field {
      position: relative;
      width: 100%;

      & > .autocomplete {
         position: absolute;
         top: 100%;
         left: 0;
         width: 100%;
         max-height: 200px;
         overflow-y: auto;
         z-index: 1000;
         outline: 1px solid #ccc;

         & > span {
            display: block;
            padding: 10px;
            background-color: #fff;
            border: 1px solid #ccc;
            cursor: pointer;

            &:hover {
               background-color: #eee;
            }
         }
      }
   }
}

.separator {
   font-size: 18px;
   font-weight: bold;
   margin: 0 10px;
}

.button {
   background-color: #7bc654 !important;
}

.trajet {
   margin-top: 20px;
   display: flex;
   flex-direction: column;
   align-items: center;
}

.station-list {
   max-height: 300px; /* Changez cette valeur en fonction de la hauteur souhaitée */
   overflow-y: auto;
   width: 100%;
   outline: 1px solid darkgray;
}

.metro-line {
   font-family: 'Poppins', sans-serif;
   display: flex;
   align-items: center;
   justify-content: center;
   width: 28px;
   height: 28px;
   border-radius: 50%;
   font-size: 14px;
   font-weight: bold;
   text-align: center;
   color: #fff;
   background-color: var(--metro-color, #7bc654);
   line-height: 28px;
}

.walk {
   font-family: 'Poppins', sans-serif;
   display: flex;
   align-items: center;
   justify-content: center;
   width: 28px;
   height: 28px;
   border-radius: 50%;
   font-size: 14px;
   font-weight: bold;
   text-align: center;
   color: #fff;
   background-color: var(#7bc654);
   line-height: 28px;
}


</style>