import Api from "$lib/api/Api";
export enum RoleName {
    ROLE_EMPLOYEE = "ROLE_EMPLOYEE",
    ROLE_SUPERVISOR = "ROLE_SUPERVISOR",
    ROLE_ADMIN = "ROLE_ADMIN",
}

export type Role = {
    name: RoleName,
}

export type User = {
    id: string
    firstname: string,
    lastname: string,
    email: string,
    password: string | undefined,
    supervisor: User,
    roles: Role[],
}

export function hasRole(user: User, roleName: RoleName): boolean {
    return user.roles.some(role => role.name === roleName);
}

class UserEndpoint extends Api {

    constructor(baseApi: Api) {
        super(baseApi.accessToken, baseApi.refreshToken);
    }

    async all() {
        try {
            const response = await this.get('users', null);
            return response as User[];
        } catch (error) {
            throw error;
        }
    }

    async getUserById(id: string) {
        try {
            const response = await this.get('users/' + id, null);
            return response as User;
        } catch (error) {
            throw error;
        }
    }

    async searchUserByName(name: string, supervisorOnly: boolean) {
        try {
            const response = await this.get('users/search/' + name, {},{supervisorOnly: supervisorOnly});
            return response as User[];
        } catch (error) {
            throw error;
        }
    }
    async save(request: User) {
        try {
            const response = await this.post('users', request);
            return response as User;
        } catch (error) {
            throw error;
        }
    }

}

export default UserEndpoint;