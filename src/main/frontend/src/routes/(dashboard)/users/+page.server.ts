import type {PageServerLoad} from './$types';
import UserEndpoint from "$lib/api/UserEndpoint";

export const load = (async ({ locals }) => {

    const userApi = new UserEndpoint(locals.api);

    const users = await userApi.all();

    let userIds = new Set<number>();
    users.map(function(val){
        if (val.id) {
            userIds.add(val.id);
            val.employees.map((employee)=>{
                if (employee.id )
                    userIds.add(employee.id);
            })
        }
    });

    return {
        userCount: userIds.size,
        users: users,
    };
}) satisfies PageServerLoad;