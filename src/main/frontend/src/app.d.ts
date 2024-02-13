// See https://kit.svelte.dev/docs/types#app
// for information about these interfaces


import type Api from "$lib/server/v1/Api";

declare global {
	namespace App {
		// interface Error {}
		// interface Locals {}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}

		interface Locals {
			isUserLoggedIn: boolean;
			user: {
				id: string;
				accessToken: string;
				refreshToken: string;
			} | null;
			api: Api
		}
	}
}

export {};
