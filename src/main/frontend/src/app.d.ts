// See https://kit.svelte.dev/docs/types#app
// for information about these interfaces
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
				email: string;
				accessToken: string;
				refreshToken: string;
			} | null;
		}
	}
}

export {};
