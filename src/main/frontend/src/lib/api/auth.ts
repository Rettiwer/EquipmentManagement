import {type ApiError, post} from "$lib/api/main";

export type AuthenticationRequest = {
    email: string,
    password: string,
}

export type AuthenticationResponse = {
    access_token: string,
    refresh_token: string
}

export const login = async (request: AuthenticationRequest) => {
    try {
        const response = await post('auth/authenticate', request, false);
        return response as AuthenticationResponse;
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