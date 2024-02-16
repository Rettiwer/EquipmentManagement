import {error, type Handle} from '@sveltejs/kit';
import { sessionManager } from '$lib/server/sessionManager';
import Api from "$lib/api/Api";
import AuthEndpoint from "$lib/api/AuthEndpoint";

export const handle: Handle = async ({ event, resolve }) => {
    let userSession = await sessionManager.getSession(await event.cookies);

    event.locals = {
        isUserLoggedIn: false,
        user: null,
        api: new Api(null, null),
    };
    if (userSession.error) {
        await sessionManager.deleteCookie(await event.cookies);
        return resolve(event);
    }
    if (userSession && userSession.data) {
        const api = new Api(userSession?.data.accessToken, userSession?.data.refreshToken);

        const userId = userSession.data.id;
        const refreshToken = userSession.data.refreshToken;

        const authEndpoint = new AuthEndpoint(api);
        await authEndpoint.ping().then(()=> {
            event.locals = {
                isUserLoggedIn: true,
                user: userSession.data,
                api: new Api(userSession.data.accessToken, userSession.data.refreshToken),
            };
        }).catch(async () => {
            await authEndpoint.refresh({userId, refreshToken})
                .then(async res => {
                    userSession = await sessionManager.updateSession(
                        await event.cookies,
                        {
                            id: res.userId,
                            accessToken: res.access_token,
                            refreshToken: res.refresh_token,
                        },
                    )
                }).catch(async error => {
                    await sessionManager.deleteCookie(await event.cookies);
                    return resolve(event);
                });
        })
    }
    return resolve(event);
};