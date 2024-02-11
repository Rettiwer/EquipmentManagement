import { get} from "$lib/api/main";

export type User = {
    id: string
    firstname: string,
    lastname: string,
    email: string,
    roles: string[],
}

export const getUserById = async (id: string, token: string) => {
    try {
        const response = await get('users/' + id, null, token);
        return response as User;
    } catch (error) {
        console.error(error);
        throw error;
    }
}