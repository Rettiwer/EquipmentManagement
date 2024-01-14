<script lang="ts">
	import Nav from "./Nav.svelte";

</script>

<main class="flex flex-col md:flex-row h-screen preview">
	<Nav/>

	<article class="bg-base-300/10 flex-1 overflow-y-scroll">
		<slot />
	</article>

</main>

<style>
	/*.preview {
		background-size: 5px 5px;
		background-image: radial-gradient(#e7e7e7 1px, #f3f4f6 0);
	}*/
</style>