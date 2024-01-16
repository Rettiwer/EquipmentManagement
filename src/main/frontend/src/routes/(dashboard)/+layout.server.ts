import type { LayoutServerLoad } from './$types';
import {redirect} from "@sveltejs/kit";

export const load = (async ({ cookies }) => {
    const sessionid = cookies.get('accessToken');

    if (!sessionid) {
        throw redirect(302, '/login');
    }
}) satisfies LayoutServerLoad;