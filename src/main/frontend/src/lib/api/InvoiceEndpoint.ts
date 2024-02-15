import type {Item, UserItems} from "$lib/api/ItemEndpoint";
import Api from "$lib/api/Api";

export type Invoice = {
    id: string | null,
    invoiceId: string,
    date: string,
    items: Item[]
}


class InvoiceEndpoint extends Api {

    constructor(baseApi: Api) {
        super(baseApi.accessToken, baseApi.refreshToken);
    }

    async getById(id: number) {
        try {
            const response = await this.get('invoices/' + id, null);
            return response as Invoice;
        } catch (error) {
            throw error;
        }
    }

    async save(request: Invoice) {
        try {
            const response = await this.post('invoices', request);
            return response as Invoice;
        } catch (error) {
            throw error;
        }
    }

    async update(request: Invoice) {
        try {
            const response = await this.put('invoices/' + request.id, request);
            return response as Invoice;
        } catch (error) {
            throw error;
        }
    }

    async deleteById(id: string) {
        try {
            await this.delete('invoices/' + id);
            return true;
        } catch (error) {
            throw error;
        }
    }
}

export default InvoiceEndpoint;