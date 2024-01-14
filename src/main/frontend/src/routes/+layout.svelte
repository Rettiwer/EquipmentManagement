<script context="module">
    import { writable } from 'svelte/store'
    export const title = writable("")

</script>
<script lang="ts">
    import '../app.css';
</script>

<svelte:head>
    <title>{$title ? `${$title} - Equipment Management` :  'Equipment Management'}</title>
    <meta name="description" content="Equipment Management is application which allows yours supervisors to manage
                                       employees equipment and assets."/>
</svelte:head>


<div class="app">
    <slot/>
</div>