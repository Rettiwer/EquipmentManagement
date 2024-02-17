import type {PageServerLoad} from '../../$types';
import {type Actions, error, redirect} from "@sveltejs/kit";
import InvoiceEndpoint, {type Invoice} from "$lib/api/InvoiceEndpoint";

export const load = (async ({ params, cookies,locals }) => {
    let invoiceId = params.id;
    if (invoiceId == null) {
        throw error(404);
    }

    const invoiceApi = new InvoiceEndpoint(locals.api);

    try {
        const invoice = await invoiceApi.getById(invoiceId);
        return {
            invoice: invoice
        };
    } catch (error) {}

    throw error(404);
}) satisfies PageServerLoad;

export const actions: Actions = {
    default: async ({ locals, request, cookies }) => {
        const formData = await request.formData();

        const id = formData.get('id') as string;
        const invoiceId = formData.get('invoice_id') as string;
        const date = formData.get('date') as string;
        const itemsField = formData.get("items") as string;

        const items: any = JSON.parse(itemsField);

        let data: Invoice = {
            id,
            invoiceId,
            date,
            items: items
        }

        try {
            const res = await new InvoiceEndpoint(locals.api).update(data);
        } catch (error)  {
            return {success: false, error: error };
        }

        redirect(303, '/invoices');
    }
};