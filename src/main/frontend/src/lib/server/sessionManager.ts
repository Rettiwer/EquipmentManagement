import { IoRedisSessionStore } from '@ethercorps/sveltekit-redis-session';
import Redis from 'ioredis';
import { SECRET, REDIS_URL } from '$env/static/private';
import {dev} from "$app/environment";

export const sessionManager = new IoRedisSessionStore ({
    redisClient: new Redis(REDIS_URL),
    secret: SECRET,
    cookieName: 'session',
    serializer: JSON,
    cookiesOptions: {
        path: '/',
        httpOnly: true,
        sameSite: 'strict',
        secure: !dev,
        maxAge: 60 * 60 * 24
    }
});