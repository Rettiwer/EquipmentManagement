import type { Handle } from '@sveltejs/kit';
import { sessionManager } from '$lib/server/sessionManager';

export const handle: Handle = async ({ event, resolve }) => {
    const userSession = await sessionManager.getSession(await event.cookies);

    event.locals = {
        isUserLoggedIn: false,
        user: null
    };
    if (userSession.error) {
        await sessionManager.deleteCookie(await event.cookies);
        return resolve(event);
    }
    if (userSession && userSession.data) {
        event.locals = {
            isUserLoggedIn: true,
            user: userSession?.data
        };
    }
    return resolve(event);
};