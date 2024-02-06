import type {LayoutServerLoad} from './$types';
import {redirect} from "@sveltejs/kit";
import {decodeJWT} from "$lib/api/utils/jwt";
import {getUserById} from "$lib/api/user";

export const load = (async ({ cookies,locals }) => {
    let authorization = cookies.get('Authorization');

    if (!authorization) {
        redirect(302,'login');
    }

    let userId = decodeJWT(authorization.split(' ')[1]).id;

    let auth = await getUserById(userId, authorization);

    return {
        auth: auth,
    };
}) satisfies LayoutServerLoad;