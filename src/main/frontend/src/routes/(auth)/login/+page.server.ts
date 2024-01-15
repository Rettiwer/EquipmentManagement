import {redirect} from "@sveltejs/kit";

/** @type {import('./$types').Actions} */
export const actions: import('./$types').Actions = {
    default: async ({ cookies, request }) => {
        const data = await request.formData();

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

            cookies.set('accessToken', result.access_token, {
                path: '/',
                sameSite: 'lax',
                httpOnly: true
            });

            cookies.set('refreshToken', result.refresh_token, {
                path: '/',
                sameSite: 'lax',
                httpOnly: true
            });

            throw redirect(303, '/equipment');
        } catch (error) {
            console.log(error);
        }
    }
};