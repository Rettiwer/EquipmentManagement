import type {LayoutServerLoad} from './$types';
import {redirect} from "@sveltejs/kit";

export const load = (async ({ cookies,locals }) => {
    let authorization = cookies.get('Authorization');

    if (!authorization) {
        redirect(302,'login');
    }
}) satisfies LayoutServerLoad;