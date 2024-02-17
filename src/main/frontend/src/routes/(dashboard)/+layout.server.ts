import type {LayoutServerLoad} from './$types';
import {redirect} from "@sveltejs/kit";
import UserEndpoint from "$lib/api/UserEndpoint";


export const load = (async ({ cookies,locals }) => {
    if (!locals.isUserLoggedIn) {
        redirect(302,'login');
    }

    const userApi = new UserEndpoint(locals.api);
    // @ts-ignore
    const user = await userApi.getById(locals.user.id);

    return {
        user: user,
        isAuthenticated: locals.isUserLoggedIn
    };
}) satisfies LayoutServerLoad;