<template>
   <div class="home">
      <h3>
         Bienvenue sur CityConnect, l'application qui vous aide à trouver le meilleur itinéraire pour vos déplacements
         urbains.
      </h3>
      <br>
      <div class="features">
         <h3>Fonctionnalités</h3>
         <ul>
            <li>Recherche d'itinéraires optimisés</li>
            <li>Prise en compte de tous les modes de transport</li>
            <li>Intégration des horaires en temps réel</li>
            <li>Alertes de perturbations et travaux</li>
            <li>Favoris et historique de vos trajets</li>
         </ul>
      </div>
      <router-link to="/trajets" class="start-button">Commencer</router-link>
   </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';

export default defineComponent({
   name: 'HomePresentation',
});
</script>

<style scoped lang="scss">
.home {
   margin-top: var(--van-nav-bar-height);
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   min-height: 40vh;
   text-align: center;
}

h1 {
   font-size: 3em;
}

.start-button {
   display: inline-block;
   background-color: #7bc654 !important;
   color: #ffffff;
   padding: 10px 20px;
   border-radius: 4px;
   font-weight: bold;
   text-decoration: none;
   margin-top: 3em;
}

.start-button:hover {
   background-color: #2a8f66;
}
</style>