import { defineStore } from 'pinia';
import { Marker } from "@/model/marker.model";
import { Polyline } from "@/model/polyline.model";

interface MapState {
   markers: Marker[];
   polylines: Polyline;
   colors: string[];
   mapLat: number;
   mapLng: number;
   depart: string;
   arrive: string;
}

export const useMapStore = defineStore('maps', {
   state: (): MapState => ({
      markers: [],
      polylines: {
         latlngs: [],
         color: []
      },
      colors: [
         "#FFCE00", // 1
         "#0064B0", // 2
         "#98D4E2", // 3
         "#C04191", // 4
         "#F28E42", // 5
         "#83C491", // 6
         "#F3A4BA", // 7
         "#CEADD2", // 8
         "#D5C900", // 9
         "#E3B32A", // 10
         "#8D5E2A", // 11
         "#00814F", // 12
         "#98D4E2", // 13
         "#662483", // 14
         "#83C491", // 15
         "#98D4E2", // 16
         "#ffffff", // 17
      ],
      mapLat: 48.85727665711052,
      mapLng: 2.314682006835938,
      depart: "",
      arrive: "",
   })
});
