<script lang="ts">
    import {IconArrowNarrowLeft} from "@tabler/icons-svelte";
    import { invalidateAll, goto } from '$app/navigation';
    import { applyAction, deserialize } from '$app/forms';
    import Input from "$lib/components/Input.svelte";
    import {hasRole, RoleName} from "$lib/api/UserEndpoint";
    import InputDropdown from "$lib/components/InputDropdown.svelte";
    import {page} from "$app/stores";
    import Checkbox from "$lib/components/Checkbox.svelte";
    import Button from "$lib/components/Button.svelte";

    export let form;

    let user = $page.data.user;

    // let employeeForm = useForm({
    //     first_name: null,
    //     last_name: null,
    //     email: null,
    //     supervisor_id: null,
    //     supervisor_name: null,
    //     roles: [],
    // });

    // const onSubmit = () => {
    //     //If user is not an admin, assign user id as supervisor id
    //     if (!user.roles.includes('ROLE_ADMIN'))
    //         $employeeForm.supervisor_id = user.id;
    //
    //     $employeeForm.post(window.route('employees.store'), {
    //         onSuccess: () => {
    //             $employeeForm.reset();
    //         },
    //     });
    // };

    // let supervisors = [];
    // async function searchSupervisor(name) {
    //     if (!name) {
    //         supervisors = [];
    //         return;
    //     }
    //
    //     const res = await axios.post(window.route('employees.search'), {name: name, supervisor: true});
    //     supervisors = res.data;
    // }

    /** @param {{ currentTarget: EventTarget & HTMLFormElement}} event */
    async function handleSubmit(event: any) {
        const data = new FormData(event.currentTarget);
       //data.append('items', JSON.stringify(items));

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
</script>
<svelte:head>
    <title>New employee</title>
</svelte:head>

<main class="flex flex-col xl:flex-row justify-around p-10">
    <section class="w-full max-w-xl mb-10 sm:mr-10 h-max">
        <a href="/users" class="text-2xl font-bold mb-3 flex items-center cursor-pointer">
            <IconArrowNarrowLeft/>
            <span class="ml-3">New employee</span>
        </a>
        <div class="card bg-base-100 shadow-md rounded-xl">
            <div class="card-body">
<!--                <ValidationErrors class="mb-4" errors={errors}/>-->


                <form on:submit|preventDefault={handleSubmit}>
                    <Input
                            type="text"
                            label="First name"
                            name="firstname"
                            placeholder="eg. Mike"
                            required
                            autocomplete="none"
                    />

                    <Input
                            type="text"
                            label="Last name"
                            name="lastname"
                            placeholder="e.g. Smith"
                            required
                            autocomplete="none"
                    />

                    <Input
                            type="text"
                            label="Email"
                            name="email"
                            requiredl
                            placeholder="e.g. someone@example.com"
                            autocomplete="none"
                    />


                    {#if hasRole(user, RoleName.ROLE_ADMIN) }

<!--                            <InputDropdown label="Supervisor"-->
<!--                                           placeholder="Search supervisor"-->
<!--                                           data={ supervisors }-->
<!--                                           bind:displayValue={$employeeForm.supervisor_name}-->
<!--                                           bind:value={$employeeForm.supervisor_id}-->
<!--                                           let:item-->
<!--                                           on:input={(e) => searchSupervisor(e.target.value) }>-->

<!--                                    <span class="mx-2 label-text text-base" data-id="{item.id}">-->
<!--                                                {item.first_name}, {item.last_name}-->
<!--                                    </span>-->
<!--                            </InputDropdown>-->

                    {/if}

                    {#if hasRole(user, RoleName.ROLE_ADMIN) }
                        <fieldset>
                            <legend>Rola</legend>

<!--                            <Checkbox value={RoleName.ROLE_ADMIN} label="Admin"-->
<!--                                          bind:groupValues={$employeeForm.roles}/>-->

<!--                            <Checkbox value={RoleName.ROLE_SUPERVISOR} label="Supervisor"-->
<!--                                      bind:groupValues={$employeeForm.roles}/>-->

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
