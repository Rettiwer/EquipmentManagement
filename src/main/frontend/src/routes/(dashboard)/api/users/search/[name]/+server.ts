import { json } from '@sveltejs/kit';
import type {RequestHandler} from './$types';
import UserEndpoint from "$lib/api/UserEndpoint";

export const GET: RequestHandler = async ({params, url, request, locals }) => {
    let searchName = params.name;
    let supervisorOnly = url.searchParams.get('supervisorOnly') != null;

    if (!searchName) return json([]);

    const userApi = new UserEndpoint(locals.api);
    const userItems = await userApi.searchUserByName(searchName, supervisorOnly);

    return json(userItems);
};
