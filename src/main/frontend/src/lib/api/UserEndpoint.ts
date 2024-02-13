import Api from "$lib/api/Api";

export type User = {
    id: string
    firstname: string,
    lastname: string,
    email: string,
    roles: string[],
}

class UserEndpoint extends Api {

    constructor(baseApi: Api) {
        super(baseApi.accessToken, baseApi.refreshToken);
    }

    async getUserById(id: string) {
        try {
            const response = await this.get('users/' + id, null);
            return response as User;
        } catch (error) {
            throw error;
        }
    }
}

export default UserEndpoint;