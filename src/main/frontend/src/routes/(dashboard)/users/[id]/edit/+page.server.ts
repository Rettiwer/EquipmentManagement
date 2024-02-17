import {type Actions, error, redirect} from "@sveltejs/kit";
import UserEndpoint, {type Role, type User} from "$lib/api/UserEndpoint";
import type {PageServerLoad} from '../../$types';

export const load = (async ({ params, cookies,locals }) => {
    let userId = params.id;
    if (userId == null) {
        throw error(404);
    }

    const userApi = new UserEndpoint(locals.api);

    try {
        const user = await userApi.getById(userId);
        return {
            employee: user
        };
    } catch (error) {}

    throw error(404);
}) satisfies PageServerLoad;

export const actions: Actions = {
    default: async ({ locals, request, cookies }) => {
        const formData = await request.formData();

        const idField = formData.get('id') as string;
        const firstNameField = formData.get('firstname') as string;
        const lastNameField = formData.get('lastname') as string;
        const emailField = formData.get("email") as string;
        const passwordField = formData.get("new-password") as string;
        const supervisorField = formData.get("supervisor") as string;
        const rolesField = formData.get("roles") as string;

        const supervisor: User = JSON.parse(supervisorField);
        const roles: Role[] = JSON.parse(rolesField);

        let data: User = {
            id: idField,
            firstname: firstNameField,
            lastname: lastNameField,
            email: emailField,
            password: passwordField.length == 0 ? null : passwordField,
            supervisor: supervisor,
            roles: roles,
        }

        try {
            const res = await new UserEndpoint(locals.api).update(data);
        } catch (error)  {
            return {success: false, error: error };
        }

        redirect(303, '/users');
    },
};