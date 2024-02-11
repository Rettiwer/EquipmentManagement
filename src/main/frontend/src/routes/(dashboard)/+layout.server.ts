import type {LayoutServerLoad} from './$types';
import {redirect} from "@sveltejs/kit";
import {getUserById} from "$lib/api/user";


export const load = (async ({ cookies,locals }) => {
    if (!locals.isUserLoggedIn) {
        redirect(302,'login');
    }

    // @ts-ignore
    const user = await getUserById(locals.user.id, locals.user.accessToken);

    return {
        user: user,
        isAuthenticated: locals.isUserLoggedIn
    };
}) satisfies LayoutServerLoad;