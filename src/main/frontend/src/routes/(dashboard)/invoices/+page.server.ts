import type {PageServerLoad} from './$types';
import UserItemsEndpoint from "$lib/api/ItemEndpoint";

export const load = (async ({ locals }) => {

    const userItemsApi = new UserItemsEndpoint(locals.api);

    const userItems = await userItemsApi.getUserItems();

    return {
        userItems: userItems,
    };
}) satisfies PageServerLoad;