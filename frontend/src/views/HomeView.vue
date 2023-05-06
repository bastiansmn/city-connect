<template>
    <div class="home">
        <HomePresentation />
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import HomePresentation from '@/components/HomePresentation.vue'; // @ is an alias to /src

export default defineComponent({
    name: 'HomeView',
    components: {
        HomePresentation,
    },
});
</script>
