<script lang="ts">
    import {IconArrowNarrowLeft} from "@tabler/icons-svelte";
    import { invalidateAll } from '$app/navigation';
    import { applyAction, deserialize } from '$app/forms';
    import Input from "$lib/components/Input.svelte";
    import {hasRole, type Role, RoleName, type User} from "$lib/api/UserEndpoint";
    import {page} from "$app/stores";
    import Button from "$lib/components/Button.svelte";
    import InputDropdown from "$lib/components/InputDropdown.svelte";
    import Checkbox from "$lib/components/Checkbox.svelte";

    export let form;

    let user = $page.data.user;

    let supervisor: User = $page.data.employee.supervisor != null ? $page.data.employee.supervisor : <User>{};

    let supervisorFullName = '';
    $: if (supervisor.id != null) {
        supervisorFullName = supervisor.firstname + ', ' + supervisor.lastname
    }

    let roleNames: Role[] = $page.data.employee.roles;

    let roles: RoleName[] = roleNames.map(role => role.name);

    let firstname = $page.data.employee.firstname;
    let lastname = $page.data.employee.lastname;
    let email = $page.data.employee.email;

    $: $page.data.employee.roles, console.log($page.data.employee.roles)

    /** @param {{ currentTarget: EventTarget & HTMLFormElement}} event */
    async function handleSubmit(event: any) {
        const data = new FormData(event.currentTarget);
        data.append('supervisor', JSON.stringify(supervisor));
        data.append('roles', JSON.stringify(roles));

        const response = await fetch(event.currentTarget.action, {
            method: 'POST',
            body: data,
            headers: {
                'x-sveltekit-action': 'true'
            }
        });

        /** @type {import('@sveltejs/kit').ActionResult} */
        const result = deserialize(await response.text());


        if (result.type === 'success') {
            await invalidateAll();
        }

        applyAction(result);
    }

    let supervisors: User[] = [];
    async function searchSupervisor(e: any) {
        let name = e.target.value;
        if (!name) {
            supervisors = [];
            return;
        }
        const response = await fetch('/api/users/search/' + name + '?' +new URLSearchParams({supervisorOnly: 'true'}), {
            method: 'GET',
            headers: {
                'content-type': 'application/json'
            }
        });

        supervisors = await response.json();
    }


</script>
<svelte:head>
    <title>New employee</title>
</svelte:head>

<main class="flex flex-col xl:flex-row justify-around p-10">
    <section class="w-full max-w-xl mb-10 sm:mr-10 h-max">
        <a href="/users" class="text-2xl font-bold mb-3 flex items-center cursor-pointer">
            <IconArrowNarrowLeft/>
            <span class="ml-3">Edit employee</span>
        </a>
        <div class="card bg-base-100 shadow-md rounded-xl">
            <div class="card-body">
                {#if !form?.success && form?.error.message != null }
                    <p class="text-red-500">{form.error.message}</p>
                {/if}

                <form on:submit|preventDefault={handleSubmit}>

                    <input
                            type="text"
                            name="id"
                            required
                            hidden
                            bind:value={$page.data.employee.id}
                    />

                    <Input
                            type="text"
                            label="First name"
                            name="firstname"
                            placeholder="eg. Mike"
                            required
                            bind:value={firstname}
                            role="presentation"
                            autocomplete="off"
                    />

                    <Input
                            type="text"
                            label="Last name"
                            name="lastname"
                            placeholder="e.g. Smith"
                            required
                            bind:value={lastname}
                            role="presentation"
                            autocomplete="off"
                    />

                    <Input
                            type="text"
                            label="Email"
                            name="email"
                            required
                            placeholder="e.g. someone@example.com"
                            bind:value={email}
                            role="presentation"
                            autocomplete="off"
                    />

                    <Input
                            type="password"
                            label="Password"
                            name="new-password"
                            role="presentation"
                            autocomplete="off"
                    />


                    {#if hasRole(user, RoleName.ROLE_ADMIN) }

                        <InputDropdown label="Supervisor"
                                       placeholder="Search supervisor"
                                       data={ supervisors }
                                       bind:displayValue={supervisorFullName}
                                       bind:value={supervisor}
                                       let:item
                                       on:input={(e) => searchSupervisor(e) }>

                                    <span class="mx-2 label-text text-base" data-item={JSON.stringify(item)}>
                                                {item.firstname}, {item.lastname}
                                    </span>
                        </InputDropdown>

                    {/if}

                    {#if hasRole(user, RoleName.ROLE_ADMIN) }
                        <fieldset>
                            <legend>Rola</legend>

                            <Checkbox value={RoleName.ROLE_ADMIN} label="Admin"
                                      bind:groupValues={roles}/>

                            <Checkbox value={RoleName.ROLE_SUPERVISOR} label="Supervisor"
                                      bind:groupValues={roles}/>

                            <Checkbox value={RoleName.ROLE_EMPLOYEE} label="Employee"
                                      bind:groupValues={roles}/>

                        </fieldset>
                    {/if}

                    <div class="flex items-center justify-end mt-4">
                        <Button class="ml-4" type="submit">
                            Save
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    </section>
</main>
