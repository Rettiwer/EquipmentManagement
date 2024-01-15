<script lang="ts">
    import Button from "$lib/components/Button.svelte";

    let employeeEquipment = [
        {
            "full_name": "Krzysztof Joszko",
            "items": [
                {
                    "name": "Test equipment item",
                    "price": 100,
                    "invoice": {
                        "invoice_id": "2022/2/2",
                    }
                }
            ]
        },
        {
            "full_name": "Krzysztof Joszko",
            "items": [
                {
                    "name": "Test equipment item",
                    "price": 100,
                    "invoice": {
                        "invoice_id": "2022/2/2",
                    }
                }
            ]
        }
    ];

</script>

<svelte:head>
    <title>Equipment - EM</title>
</svelte:head>

<section class="flex flex-col xl:flex-row justify-around p-10">
    <section class="w-full max-w-4xl sm:mr-10 h-max">

        { #if employeeEquipment.length > 0}

            { #each employeeEquipment as employee, i }
                <section class="mb-8">
                    <h1 class="text-2xl font-bold mb-3">{ employee.full_name }</h1>
                    <div class="overflow-x-auto bg-base-100 shadow-md rounded-xl bg-base-300">
                        <table class="table table-normal w-full">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th class="lg:w-40 text-center">Invoice Id</th>
                                <th class="lg:w-40 text-center">Price [EUR]</th>
                            </tr>
                            </thead>
                            <tbody>
                            { #if employee['items']}
                                { #each employee['items'] as item }
                                    <tr class="hover">
                                        <td class="flex-1">{item.name}</td>
                                        <td class="lg:w-40 text-center">{item.invoice.invoice_id}</td>
                                        <td class="lg:w-40 text-center">{item.price}</td>
                                    </tr>
                                {/each}
                            {/if}
                            </tbody>
                        </table>
                    </div>
                </section>
            {/each}
        { :else }
            <div class="card w-full bg-base-100 text-accent">
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

            <div class="flex flex-col justify-center m-4 sm:flex-row sm:space-bettween">
                <Button class="mb-4 sm:mb-0">
                    New Invoice
                </Button>
            </div>
        </div>
    </section>
</section>

<style>
    .liquidatedItem {
        background-image: repeating-linear-gradient(45deg, hsl(var(--b1)), hsl(var(--b2)) 13px, hsl(var(--b2)) 13px, hsl(var(--b2)) 14px);
    }
</style>

