import type {PageServerLoad} from './$types';
import UserItemsEndpoint from "$lib/api/ItemEndpoint";

export const load = (async ({ locals }) => {

    const userItemsApi = new UserItemsEndpoint(locals.api);

    const userItems = await userItemsApi.getUserItems();

    let equipmentCount = 0;
    userItems.map(function(val){
        equipmentCount += val.items.length;
    });

    return {
        equipmentAmount: equipmentCount,
        userItems: userItems,
    };
}) satisfies PageServerLoad;