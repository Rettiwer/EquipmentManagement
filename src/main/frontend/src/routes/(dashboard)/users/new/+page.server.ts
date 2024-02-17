import {type Actions, redirect} from "@sveltejs/kit";
import UserEndpoint, {type Role, type User} from "$lib/api/UserEndpoint";

export const actions: Actions = {
    default: async ({ locals, request, cookies }) => {
        const formData = await request.formData();

        const firstNameField = formData.get('firstname') as string;
        const lastNameField = formData.get('lastname') as string;
        const emailField = formData.get("email") as string;
        const passwordField = formData.get("password") as string;
        const supervisorField = formData.get("supervisor") as string;
        const rolesField = formData.get("roles") as string;

        const supervisor: User = JSON.parse(supervisorField);
        const roles: Role[] = JSON.parse(rolesField);

        let data: User = {
            id: '',
            firstname: firstNameField,
            lastname: lastNameField,
            email: emailField,
            password: passwordField,
            supervisor: supervisor,
            roles: roles,
        }

        try {
            const res = await new UserEndpoint(locals.api).save(data);
        } catch (error)  {
            return {success: false, error: error };
        }

        redirect(303, '/users');
    },
};