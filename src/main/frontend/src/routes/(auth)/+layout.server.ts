import {redirect} from "@sveltejs/kit";
import type {LayoutServerLoad} from "../../../.svelte-kit/types/src/routes/(dashboard)/$types";

export const ssr = false;

export const load = (async ({ cookies }) => {
    const sessionid = cookies.get('Authorization');

    if (sessionid) {
        throw redirect(302, '/items');
    }
}) satisfies LayoutServerLoad;