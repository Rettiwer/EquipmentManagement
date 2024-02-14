import {type Actions, redirect} from "@sveltejs/kit";

export const actions: Actions = {
    default: async ({ locals, request, cookies }) => {
        const formData = await request.formData();

        console.log(...formData);
    },
};