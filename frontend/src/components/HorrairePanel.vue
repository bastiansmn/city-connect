<template>
   <div class="left-panel">
      <h3 class="titleM">Veuillez choisir votre arrêt métros et la ligne:</h3>
      <div class="input-container">
         <div class="input-field">
            <van-field
                  class="custom-field"
                  list="first-station"
                  rows="3"
                  v-model="metro1"
                  name="Username"
                  autocomplete="off"
                  placeholder="Métro"
                  :rules="[{ required: true, message: 'Metro is required' }]"
            />
            <div class="autocomplete" v-if="autocompleteMetro1.length > 0" id="first-station">
               <span @click="fillInput1(station)" :key="station" v-for="station in autocompleteMetro1">{{
                     station
                  }}</span>
            </div>
         </div>
         <span class="separator">➡</span>
         <van-dropdown-menu class="custom-field custom-dropdown">
            <van-dropdown-item v-model="value1" :options="option1"/>
         </van-dropdown-menu>
      </div>
      <div class="input-container-bis">
         <van-dropdown-menu class="custom-field custom-dropdown-bis">
            <van-dropdown-item v-model="value2" :options="option2"/>
         </van-dropdown-menu>
         <van-button class="button" type="success" @click="sendMetros">Confirmer</van-button>
      </div>
      <div class="trajet">
         <h3 class="titleM">Voici les horaires :</h3>
         <div class="station-list">
            <van-cell-group>
               <van-cell
                     v-for="(station, index) in horaireData"
                     :key="index"
                     :title="station.horraire"
               />
            </van-cell-group>
         </div>
      </div>
   </div>
</template>

<script setup lang="ts">
import {Marker} from "@/model/marker.model";
import {useMapStore} from '@/store/map.store'
import {Polyline} from "@/model/polyline.model";
import {ref} from "vue";


const metro1 = ref("");
const horaireData = ref([]);
const value1 = ref(0);
const option1 = ref([]);


const value2 = ref(0);
const option2 = ref([]);

const autocompleteMetro1 = ref<string[]>([]);


const ligne = ref(option1.value.find((opt) => opt.value === value1.value)?.value || '');
const direction = ref(option2.value.find((opt) => opt.value === value2.value)?.value || '');
const fillInput1 = (station: string) => {
   metro1.value = station;
   autocompleteMetro1.value = [];
};

async function autocompleteStation(station: string): Promise<string[]> {
   const response = await fetch(`/api/autocomplete?station=${station}`);
   return await response.json();
}


const store = useMapStore();

store.markers = [];
store.polylines.latlngs = [];
store.polylines.color = [];

async function sendMetros() {
   const station = metro1.value;
   direction.value = option2.value.find((opt) => opt.value === value2.value)?.value || '';

   const url = `/api/horaires?station=${station}&ligneString=${ligne.value}&direction=${direction.value}`;
   const response = await fetch(url);

   horaireData.value = await response.json();


   store.mapLat = horaireData.value[0].lon;
   store.mapLng = horaireData.value[0].lat;

   const marker: Marker = {
      draggable: false,
      position: {lat: horaireData.value[0].lon, lng: horaireData.value[0].lat},
      title: "London"
   } as Marker;

   store.markers = [marker];
}

watch(metro1, fetchLignes);


watch(value1, async () => {
   ligne.value = option1.value.find((opt) => opt.value === value1.value)?.value || '';
   const url = `/api/Direction?ligneString=${ligne.value}`;
   const response = await fetch(url);
   const direction = await response.json();
   //we get two direction put it in option2
   option2.value = direction.map((ligne: any) => ({text: ligne.direction, value: ligne.direction}));
});

async function fetchLignes() {
   const station = metro1.value;
   try {
      const url = `/api/Ligne?station=${station}`;
      const response = await fetch(url);
      const lignes = await response.json();
      option1.value = lignes.map((ligne: any) => ({text: "Ligne " + ligne.ligne, value: parseInt(ligne.ligne)}));

   } catch (e) {
      option1.value = [];
      option2.value = [];
      console.log(e);
   }

   autocompleteStation(metro1.value).then((stations) => {
      if (stations.length > 0) {
         autocompleteMetro1.value = stations;
      } else {
         autocompleteMetro1.value = [];
      }
   });
   store.depart = metro1.value;

}

</script>

<script lang="ts">
import {defineComponent, watch} from "vue";
import {Cell, CellGroup, Button, Field, DropdownMenu, DropdownItem} from 'vant';

export default defineComponent({
   name: "HorrairePanel",
   components: {
      VanButton: Button,
      VanField: Field,
      VanCell: Cell,
      VanCellGroup: CellGroup,
   },
});
</script>

<style scoped lang="scss">
.left-panel {
   flex: 1;
   width: 20%;
   background-color: #ebefea !important;
   box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
   display: flex;
   flex-direction: column;
   justify-content: flex-start;
   height: 100vh;
   padding: var(--van-nav-bar-height) !important;
}

.custom-field {
   border: 1px solid black;

}

.custom-dropdown {
   width: 300px;
}


.custom-dropdown-bis {
   width: 200px;
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
   align-items: center;
   gap: 10px;
   z-index: 10;
}

.input-container-bis {
   margin-top: 20px;
   display: flex;
   align-items: center;
   gap: 10px;
}

.input-container > .input-field {
   position: relative;
}

.input-container > .input-field > .autocomplete {
   position: absolute;
   top: 100%;
   left: 0;
   width: 100%;
   max-height: 200px;
   overflow-y: auto;
}

.input-container > .input-field > .autocomplete > span {
   display: block;
   padding: 10px;
   background-color: #fff;
   border: 1px solid #ccc;
   cursor: pointer;
}

.input-container > .input-field > .autocomplete > span:hover {
   background-color: #eee;
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
}
</style>