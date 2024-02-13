import Api from "$lib/api/Api";

export type AuthenticationRequest = {
    email: FormDataEntryValue | null,
    password: FormDataEntryValue | null,
}

type AuthenticationResponse = {
    userId: string,
    access_token: string,
    refresh_token: string
}

interface RefreshTokenRequest {
    userId: string;
    refreshToken: string
}

class AuthEndpoint extends Api {

    constructor(baseApi: Api) {
        super(baseApi.accessToken, baseApi.refreshToken);
    }

    async authenticate(request: AuthenticationRequest) {
        try {
            const response = await this.post('auth/authenticate', request,false);
            return response as AuthenticationResponse;
        } catch (error) {
            console.error(error);
            throw error;
        }
    }

    async ping() {
        try {
            return await this.get('auth/ping', null, false);
        } catch (error) {
            throw error;
        }
    }

    async refresh(request: RefreshTokenRequest) {
        try {
            return await this.post('auth/refresh', request, false);
        } catch (error) {
            throw error;
        }
    }
}

export default AuthEndpoint;