import type {LayoutServerLoad} from './$types';
import {redirect} from "@sveltejs/kit";

export let ssr = true;

export const load = (async ({ cookies, locals }) => {
    if (locals.isUserLoggedIn) {
        redirect(302,'/');
    }

}) satisfies LayoutServerLoad;