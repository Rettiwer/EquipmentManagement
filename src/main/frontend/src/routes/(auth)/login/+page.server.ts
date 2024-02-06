import type { PageServerLoad, Actions } from './$types';
import { redirect, fail } from '@sveltejs/kit';

export const load: PageServerLoad = (event) => {
    const user = event.locals.user;

    if (user) {
        throw redirect(302, '/items');
    }
};

/** @type {import("./$types").Actions} */
export const actions: Actions = {
    default: async (event) => {
        const data = await event.request.formData();

        try {
            const res = await fetch('http://127.0.0.1/api/auth/authenticate', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(Object.fromEntries(data))
            });

            const result = await res.json();

            if(!res.ok) {
                return { success: false, error: res.status, message: result.message };
            }

            console.log(result);

            let accessTokenTime = new Date();
            accessTokenTime.setDate(accessTokenTime.getDate() + 1);
            event.cookies.set('accessToken', result.access_token, {
                path: '/',
                sameSite: 'lax',
                httpOnly: true,
                expires: accessTokenTime,
                secure: false
            });

            let refreshTokenTime = new Date();
            refreshTokenTime.setDate(refreshTokenTime.getDate() + 7);
            event.cookies.set('refreshToken', result.refresh_token, {
                path: '/',
                sameSite: 'lax',
                httpOnly: true,
                expires: refreshTokenTime,
                secure: false
            });

          //  event.locals.user.id = result.id;

            console.log(event.locals.user);

            redirect(303, '/items');
        } catch (error) {
            console.log(error);
        }
    }
};