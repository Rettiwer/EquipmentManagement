import Api from "$lib/api/Api";
import type {User} from "$lib/api/UserEndpoint";
import type {Invoice} from "$lib/api/InvoiceEndpoint";

export type Item = {
    id: string
    name: string,
    price: string,
    comment: string,
    owner: User,
    invoice: Invoice | null,

}

export type UserItems = {
    id: string
    firstname: string,
    lastname: string,
    supervisorId: string,
    items: Item[],
}

class UserItemsEndpoint extends Api {

    constructor(baseApi: Api) {
        super(baseApi.accessToken, baseApi.refreshToken);
    }

    async getUserItems() {
        try {
            const response = await this.get('users/items', null);
            return response as UserItems;
        } catch (error) {
            throw error;
        }
    }
}

export default UserItemsEndpoint;