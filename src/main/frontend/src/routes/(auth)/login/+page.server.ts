import {type Actions, error, redirect} from '@sveltejs/kit';
import { sessionManager } from '$lib/server/sessionManager';
import {type AuthenticationRequest, login} from "$lib/api/auth";

export const actions: Actions = {
    default: async ({ request, cookies }) => {
        const formData = await request.formData();

        let data: AuthenticationRequest = {
            email: formData.get('email'),
            password: formData.get('password'),
        }

        try {
            const res = await login(data);

            await sessionManager.createSession(
                cookies,
                {
                    id: res.userId,
                    email: formData.get('email'),
                    accessToken: res.access_token,
                    refreshToken: res.refresh_token,
                },
                res.userId
            );
        } catch (error)  {
            return {success: false, error: error };
        }

        redirect(303, '/items');
    }
};