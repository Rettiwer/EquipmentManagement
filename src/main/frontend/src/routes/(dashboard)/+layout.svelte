<script lang="ts">
	import Nav from "./Nav.svelte";
	import {onMount} from "svelte";
	import {decodeJWT} from "$lib/api/utils/jwt";
	import {getUserById, type User} from "$lib/api/user";
	import Cookies from "js-cookie";
	import user from "$lib/components/stores/user";

	onMount(async () => {
		let authorizationToken = Cookies.get("Authorization");
		let userId = decodeJWT(authorizationToken.split(' ')[1]).userId;

		let auth = await getUserById(userId);
		$user.auth = auth as User;
	});
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