import { post } from "$lib/api/main";

export type AuthenticationRequest = {
    email: FormDataEntryValue | null,
    password: FormDataEntryValue | null,
}

export type AuthenticationResponse = {
    userId: string,
    access_token: string,
    refresh_token: string
}

export const login = async (request: AuthenticationRequest) => {
    try {
        const response = await post('auth/authenticate', request, '',false);
        return response as AuthenticationResponse;
    } catch (error) {
        console.error(error);
        throw error;
    }
}