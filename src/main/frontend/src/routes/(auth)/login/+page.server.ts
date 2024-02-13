import {type Actions, error, redirect} from '@sveltejs/kit';
import { sessionManager } from '$lib/server/sessionManager';
import AuthEndpoint, {type AuthenticationRequest} from "$lib/api/AuthEndpoint";

export const actions: Actions = {
    default: async ({ locals, request, cookies }) => {
        const formData = await request.formData();

        let data: AuthenticationRequest = {
            email: formData.get('email'),
            password: formData.get('password'),
        }

        try {
            const res = await new AuthEndpoint(locals.api).authenticate(data);

            await sessionManager.createSession(
                cookies,
                {
                    id: res.userId,
                    accessToken: res.access_token,
                    refreshToken: res.refresh_token,
                },
                res.userId
            );
        } catch (error)  {
            return {success: false, error: error };
        }

        redirect(303, '/invoices');
    }
};