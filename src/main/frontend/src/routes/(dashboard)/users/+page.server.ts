import type {PageServerLoad} from './$types';
import UserEndpoint from "$lib/api/UserEndpoint";

export const load = (async ({ locals }) => {

    const userApi = new UserEndpoint(locals.api);

    const users = await userApi.all();

    return {
        users: users,
    };
}) satisfies PageServerLoad;