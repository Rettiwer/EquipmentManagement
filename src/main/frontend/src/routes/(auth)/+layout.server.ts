import {redirect} from "@sveltejs/kit";
import type {LayoutServerLoad} from "../../../.svelte-kit/types/src/routes/(dashboard)/$types";

export const load = (async ({ cookies }) => {
    const sessionid = cookies.get('accessToken');

    if (sessionid) {
        throw redirect(302, '/equipment');
    }
}) satisfies LayoutServerLoad;