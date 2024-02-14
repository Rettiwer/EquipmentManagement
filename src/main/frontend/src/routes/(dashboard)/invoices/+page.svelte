<script lang="ts">
    import {page} from "$app/stores";
    import {hasRole, RoleName} from "$lib/api/UserEndpoint";

    $: $page, console.log($page);
</script>

<svelte:head>
    <title>Equipment - EM</title>
</svelte:head>

<section class="flex flex-col xl:flex-row justify-around p-10">
    <section class="w-full max-w-4xl sm:mr-10 h-max">

        { #if $page.data.userItems.length > 0}

            { #each $page.data.userItems as employee, i }
                <section class="mb-8">
                    <h1 class="text-2xl font-bold mb-3">{ employee.firstname + ' ' + employee.lastname }</h1>
                    <div class="overflow-x-auto shadow-md rounded-xl bg-base-300">
                        { #if employee.items.length > 0 }
                        <table class="table table-normal w-full">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th class="lg:w-40 text-center">Invoice Id</th>
                                <th class="lg:w-40 text-center">Price [EUR]</th>
                                {#if hasRole($page.data.user, RoleName.ROLE_ADMIN)}
                                    <th class="lg:w-20 text-center"></th>
                                {/if}

                            </tr>
                            </thead>
                            <tbody>
                                { #each employee.items as item }
                                    <tr class="hover">
                                        <td class="flex-1">{item.name}</td>
                                        <td class="lg:w-40 text-center">{item.invoice.invoiceId}</td>
                                        <td class="lg:w-40 text-center">{item.price}</td>
                                        {#if hasRole($page.data.user, RoleName.ROLE_ADMIN)}
                                            <td class="lg:w-fit text-center">
                                                <a class="btn btn-sm btn-primary" href={`/invoices/${item.invoice.id}/edit`}>
                                                    EDIT
                                                </a>
                                            </td>
                                        {/if}
                                    </tr>
                                {/each}
                            </tbody>
                        </table>
                        { :else }
                            <div class="card w-full bg-base-300 text-primary">
                                <div class="card-body items-center text-center">
                                    <h2 class="card-title">No items</h2>
                                </div>
                            </div>
                        { /if }
                    </div>
                </section>
            {/each}
        { :else }
            <div class="card w-full bg-base-300 text-accent">
                <div class="card-body items-center text-center">
                    <h2 class="card-title">No items</h2>
                </div>
            </div>
        {/if}
    </section>

    <section class="sticky top-10 self-start w-full xl:w-fit">
        <h1 class="text-2xl font-bold mb-3">Summary</h1>
        <div class="card card-compact bg-base-100 shadow-xl">
            <div class="stats stats-vertical lg:stats-horizontal">

                <div class="stat">
                    <div class="stat-title">Amount of equipment</div>
                    <div class="stat-value">5</div>
                </div>
            </div>

            <hr>

            <div class="flex flex-col justify-center m-4 sm:flex-row sm:space-between">
                <a href="/invoices/new" class="btn btn-primary mb-4 sm:mb-0">
                    New Invoice
                </a>
            </div>
        </div>
    </section>
</section>