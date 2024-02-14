import { json } from '@sveltejs/kit';
import type {RequestEvent, RequestHandler} from './$types';
import UserEndpoint from "$lib/api/UserEndpoint";

export const GET: RequestHandler = async ({params, request, locals }) => {
    let searchName = params.name;

    if (!searchName) return json([]);

    const userApi = new UserEndpoint(locals.api);
    const userItems = await userApi.searchUserByName(searchName);

    return json(userItems);
};
