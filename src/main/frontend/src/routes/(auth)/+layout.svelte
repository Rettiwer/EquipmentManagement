<script lang="ts">
    import Logo from "$lib/components/Logo.svelte";
</script>


<main class="flex h-dvh flex-col place-content-evenly items-center p-5 bg-neutral">
    <Logo/>

    <slot />
</main>