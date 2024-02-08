import {type ApiError, get} from "$lib/api/main";


export type User = {
    id: string
    firstname: string,
    lastname: string
}

export const getUserById = async (id: string) => {
    try {
        const response = await get('users/' + id, null);
        return response as User;
    } catch (error) {
        if (!error.response) {
            let networkError = <ApiError>{status: -1, message: "Network error has occurred.", debugMessage: error.code};
            return Promise.reject(networkError);
        }
        if (error.response.status >= 400 && error.response.status < 500) {
            throw error.response.data as ApiError;
        }
        console.error(error);
    }
}