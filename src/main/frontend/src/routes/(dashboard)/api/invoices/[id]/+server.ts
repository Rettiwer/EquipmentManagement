import type {RequestHandler} from './$types';
import InvoiceEndpoint from "$lib/api/InvoiceEndpoint";
import {json, redirect} from "@sveltejs/kit";

export const DELETE: RequestHandler = async ({params, request, locals }) => {
    let invoiceId: string = params.id;

    const invoiceApi = new InvoiceEndpoint(locals.api);

    await invoiceApi.deleteById(invoiceId);

    return json({
        redirect: '/invoices'
    });
};
