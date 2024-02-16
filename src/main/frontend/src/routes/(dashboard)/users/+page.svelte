<script lang="ts">
    import {page} from "$app/stores";
    import {hasRole, RoleName} from "$lib/api/UserEndpoint";
    import Button from "$lib/components/Button.svelte";


    $: $page, console.log($page.data)
</script>

<svelte:head>
    <title>Equipment - EM</title>
</svelte:head>

<main class="flex flex-col xl:flex-row justify-around p-10">
    <section class="w-full max-w-4xl mb-10 sm:mr-10 h-max">
        {#if $page.data.users.length > 0}
            {#each  $page.data.users as user}
                <section class="mb-8">
                    <div class="flex items-center mb-3">
                        <h1 class="text-2xl font-bold">{ user.firstname + ', ' + user.lastname }</h1>

                        <Button class="btn-xs ml-4" outlined>
                            <a href={`/users/${user.id}/edit`}>
                                EDIT
                            </a>
                        </Button>
                    </div>
                    {#if user.employees.length > 0 }
                        <div class="overflow-x-auto bg-base-300 shadow-md rounded-xl">
                            <table class="table lg:table-fixed w-full">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Firstname</th>
                                    <th>Lastname</th>
                                    <th class="lg:w-64 text-center">Role</th>
                                    <th class="lg:w-40 text-center"></th>
                                </tr>
                                </thead>
                                <tbody>
                                { #each user.employees as employee }
                                    <tr>
                                        <td>{employee.id}</td>
                                        <td>{employee.firstname}</td>
                                        <td>{employee.lastname}</td>
                                        <td class="lg:w-64 text-center">
                                            {#if employee['roles'].length > 0}
                                                { #each employee.role as role }
                                                    <div class="badge badge-accent mb-1">{ role.name }</div>
                                                    <br/>
                                                { /each }
                                            { :else }
                                                <div class="badge badge-accent">Employee</div>
                                            {/if}
                                        </td>
                                        <th class="lg:w-40 text-center">
                                            <Button class="btn-xs ml-4">
<!--                                                <a use:inertia="{{ replace: true }}" href={window.route('employees.edit', employee.id)}>-->
<!--                                                    EDYTUJ-->
<!--                                                </a>-->
                                            </Button>
                                        </th>
                                    </tr>
                                { /each }
                            </table>
                        </div>
                    {:else }
                        <div class="card w-full  bg-base-300 text-primary">
                            <div class="card-body items-center text-center">
                                <h2 class="card-title">No employees</h2>
                            </div>
                        </div>
                    {/if}
                </section>
            {/each}
        { :else }
            <div class="card w-full bg-base-100 text-accent">
                <div class="card-body items-center text-center">
                    <h2 class="card-title">Not found</h2>
                </div>
            </div>
        {/if}
    </section>

    <section class="sticky top-10 self-start w-full xl:w-fit">
        <h1 class="text-2xl font-bold mb-3">Summary</h1>
        <div class="card card-compact bg-base-100 shadow-xl">
            <div class="stats stats-vertical lg:stats-horizontal">

                <div class="stat">
                    <div class="stat-title">Employees</div>
                    <div class="stat-value">5</div>
                </div>
            </div>

            <hr>

            <div class="flex flex-col justify-center m-4 sm:flex-row sm:space-between">
                <a href="/users/new" class="btn btn-primary mb-4 sm:mb-0">
                    Dodaj pracownika
                </a>
            </div>
        </div>
    </section>
</main>
