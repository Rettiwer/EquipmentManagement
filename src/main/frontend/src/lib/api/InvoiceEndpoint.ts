import type {Item} from "$lib/api/ItemEndpoint";

export type Invoice = {
    id: number,
    invoiceId: string,
    invoiceDate: string,
    items: Item[]
}