import {type Actions, redirect} from "@sveltejs/kit";
import InvoiceEndpoint, {type Invoice} from "$lib/api/InvoiceEndpoint";

export const actions: Actions = {
    default: async ({ locals, request, cookies }) => {
        const formData = await request.formData();

        const invoiceId = formData.get('invoice_id') as string;
        const date = formData.get('date') as string;
        const itemsField = formData.get("items") as string;

        const items: any = JSON.parse(itemsField);

        let data: Invoice = {
            id: null,
            invoiceId,
            date,
            items
        }

        try {
            const res = await new InvoiceEndpoint(locals.api).save(data);
        } catch (error)  {
            return {success: false, error: error };
        }

        redirect(303, '/users');
    },
};